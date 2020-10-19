/**
 * This driver module supports both the ISP itself and the ISP Wrapper
 * by providing the user with two separate UIO devices in /dev, one for the
 * ISP and one for the ISP Wrapper. The struct uio_isp_platform_data contains
 * the uio_info for the ISP as well as the ISP Wrapper.
 * The ISP has to be defined in the Device Tree using the following structure
    isp0: isp@fec00000 {
        compatible = "renesas,isp-r8a77970";
        reg = <0 0xfec00000 0 0x20000>, <0 0xfed00000 0 0x10000>;
        interrupts = <GIC_SPI 26 IRQ_TYPE_LEVEL_HIGH>, <GIC_SPI 25 IRQ_TYPE_LEVEL_HIGH>;
        clocks = <&cpg CPG_MOD 817>;
        power-domains = <&sysc R8A77970_PD_ALWAYS_ON>;
    };
 * The first reg entry is the base and size of the ISP memory area.
 * The second reg entry is the base and size of the ISP Wrapper memory area.
 * The first interrupt entry is for the ISP.
 * The second interrupt entry is for the ISP Wrapper
 */

#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/clk.h>
#include <linux/dma-mapping.h>
#include <linux/platform_device.h>
#include <linux/uio_driver.h>
#include <linux/io.h>
#include <linux/irq.h>
#include <linux/irqdomain.h>
#include <linux/pm_runtime.h>

#include <linux/of.h>
#include <linux/of_irq.h>


/* ISP internals */
#define ISP_INTERNAL_REG_SIZE    0X20000
#define ISP_INT_FRAME_START_MASK    0x84
#define ISP_INT_FRAME_START_STATUS    0x88
#define ISP_INT_FRAME_START_CLEAR    0x8c
#define ISP_INT_FRAME_END_MASK        0x98
#define ISP_INT_FRAME_END_STATUS    0x9c
#define ISP_INT_FRAME_END_CLEAR        0xa0
#define ISP_INT_STATS_MASK        0xb0
#define ISP_INT_STATS_STATUS        0xb4
#define ISP_INT_STATS_CLEAR        0xb8

/* ISP Wrapper internals */
#define ISPVCR                0x00
#define ISPINT_STATUS            0x40
#define ISPINT_ENABLE            0x60
#define ISPERR0_ENABLE            0x64

/* ISP uio driver name */
#define DRIVER_NAME "uio_isp"
#define DRIVER_VERSION "0.1"

/* Platform data for ISP and ISP Wrapper */
struct uio_isp_platform_data
{
    struct platform_device    *pdev;
    struct uio_info        *uio_info[2];    /* [0]: ISP [1]: ISP Wrapper */
    struct clk        *isp_clock;
    spinlock_t        isp_lock;
    spinlock_t        wrapper_lock;
    unsigned long        isp_flags;
    unsigned long        wrapper_flags;
    void __iomem        *base_addr;
    void __iomem        *wrapper_addr;
};

/* ISP Wrapper function prototypes */
static int isp_wrapper_probe(struct platform_device *pdev, struct uio_isp_platform_data * const priv);
static int isp_wrapper_open(struct uio_info *uio_info, struct inode *inode);
static int isp_wrapper_close(struct uio_info *uio_info, struct inode *inode);
static irqreturn_t isp_wrapper_irq_handler(int irq, struct uio_info *uio_info);

/* ISP function prototypes */
static int isp_probe(struct platform_device *pdev, struct uio_isp_platform_data * const priv);
static int isp_open(struct uio_info *uio_info, struct inode *inode);
static int isp_close(struct uio_info *uio_info, struct inode *inode);
static irqreturn_t isp_irq_handler(int irq, struct uio_info *uio_info);

/* Common functions for ISP and ISP Wrapper */
static int irq_control(struct uio_info *uio_info, s32 irq_on);

static inline u32 read_reg(void * const base, const u32 offset)
{
    return ioread32((u8 *)base + offset);
}

static inline void write_reg(void * const base, const u32 offset, const u32 val)
{
    iowrite32(val, (u8 *)base + offset);
}

/* Function definitions */
static int isp_wrapper_probe(struct platform_device *pdev, struct uio_isp_platform_data * const priv)
{
    struct uio_info    *uio_info;
    struct uio_mem *uio_mem;
    struct resource *res;
    char *wrapper_name;
    int err;
    struct device *dev = &pdev->dev;

    /* Construct the uio_info structure */
    uio_info = devm_kzalloc(dev, sizeof(*uio_info), GFP_KERNEL);
    wrapper_name = devm_kmalloc(dev, strlen(pdev->name) + strlen("_wrapper") + 1, GFP_KERNEL);
    if (!uio_info || !wrapper_name)
        return -ENOMEM;
    sprintf(wrapper_name, "%s_wrapper", pdev->name);
    uio_info->version = DRIVER_VERSION;
    uio_info->open = isp_wrapper_open;
    uio_info->release = isp_wrapper_close;
    uio_info->name = wrapper_name;

    /* Set up memory resource */
    uio_mem = &uio_info->mem[0];
    res = platform_get_resource(pdev, IORESOURCE_MEM, 1);
    if (!res) {
        dev_err(dev, "failed to get ISP Wrapper memory\n");
        return -EINVAL;
    }
    uio_mem->memtype = UIO_MEM_PHYS;
    uio_mem->addr = res->start;
    uio_mem->size = resource_size(res);
    uio_mem->name = res->name;
    if (!devm_request_mem_region(dev, res->start, resource_size(res), uio_info->name)) {
        dev_err(dev, "failed to reserve ISP Wrapper memory\n");
        return -ENOMEM;
    }
    priv->wrapper_addr = devm_ioremap_nocache(dev, res->start, resource_size(res));
    if (!priv->wrapper_addr) {
        dev_err(dev, "failed to map ISP Wrapper memory\n");
        return -EIO;
    }

    /* Set up interrupt */
    write_reg(priv->wrapper_addr, ISPINT_ENABLE, 0);
    write_reg(priv->wrapper_addr, ISPERR0_ENABLE, 0);
    uio_info->irq = irq_of_parse_and_map(pdev->dev.of_node, 1);
    if (uio_info->irq == -ENXIO) {
        dev_err(dev, "failed to parse ISP Wrapper interrupt\n");
        return -EINVAL;
    }
    uio_info->handler = isp_wrapper_irq_handler;
    uio_info->irqcontrol = irq_control;

    /* Register the UIO device */
    if ((err = uio_register_device(dev, uio_info))) {
        dev_err(dev, "failed to register UIO device for ISP Wrapper\n");
        return err;
    }

    /* Register this uio_info with the platform data */
    uio_info->priv = priv;
    priv->uio_info[1] = uio_info;
    spin_lock_init(&priv->wrapper_lock);
    priv->wrapper_flags = 1;    /* The ISP Wrapper interrupt is active */

    /* Print some information */
    dev_dbg(dev, "ISP Wrapper irq %ld\n", uio_info->irq);

    return 0;
}

static int isp_wrapper_open(struct uio_info *uio_info, struct inode *inode)
{
    struct uio_isp_platform_data *priv = uio_info->priv;

    dev_dbg(&priv->pdev->dev, "isp wrapper open\n");

    return 0;
}

static int isp_wrapper_close(struct uio_info *uio_info, struct inode *inode)
{
    struct uio_isp_platform_data *priv = uio_info->priv;

    dev_dbg(&priv->pdev->dev, "isp wrapper close\n");

    return 0;
}

static irqreturn_t isp_wrapper_irq_handler(int irq, struct uio_info *uio_info)
{
    struct uio_isp_platform_data *priv = uio_info->priv;
    u32 ispint_status = read_reg(priv->wrapper_addr, ISPINT_STATUS);

    /* Mask all interrupts */
    write_reg(priv->wrapper_addr, ISPINT_ENABLE, 0);

    if (!ispint_status)
        return IRQ_NONE;

    return IRQ_HANDLED;
}

static int isp_probe(struct platform_device *pdev, struct uio_isp_platform_data * const priv)
{
    struct uio_info    *uio_info;
    struct uio_mem *uio_mem;
    struct resource *res;
    int err;
    struct device *dev = &pdev->dev;

    /* Construct the uio_info structure */
    uio_info = devm_kzalloc(dev, sizeof(*uio_info), GFP_KERNEL);
    if (!uio_info)
        return -ENOMEM;
    uio_info->version = DRIVER_VERSION;
    uio_info->open = isp_open;
    uio_info->release = isp_close;
    uio_info->name = pdev->name;

    /* Set up memory resource */
    uio_mem = &uio_info->mem[0];
    res = platform_get_resource(pdev, IORESOURCE_MEM, 0);
    if (!res) {
        dev_err(dev, "failed to get ISP memory\n");
        return -EINVAL;
    }
    uio_mem->memtype = UIO_MEM_PHYS;
    uio_mem->addr = res->start;
    uio_mem->size = resource_size(res);
    uio_mem->name = res->name;
    if (!devm_request_mem_region(dev, res->start, resource_size(res), uio_info->name)) {
        dev_err(dev, "failed to reserve ISP memory\n");
        return -ENOMEM;
    }
    priv->base_addr = devm_ioremap_nocache(dev, res->start, resource_size(res));
    if (!priv->base_addr) {
        dev_err(dev, "failed to map ISP memory\n");
        return -EIO;
    }

    /* Set up interrupt */
    write_reg(priv->base_addr, ISP_INT_FRAME_START_MASK, 0);
    write_reg(priv->base_addr, ISP_INT_FRAME_END_MASK, 0);
    write_reg(priv->base_addr, ISP_INT_STATS_MASK, 0);
    uio_info->irq = irq_of_parse_and_map(pdev->dev.of_node, 0);
    if (uio_info->irq == -ENXIO) {
        dev_err(dev, "failed to parse ISP interrupt\n");
        return -EINVAL;
    }
    uio_info->handler = isp_irq_handler;
    uio_info->irqcontrol = irq_control;

    /* Set up clock */
    priv->isp_clock = devm_clk_get(dev, NULL);
    if (IS_ERR(priv->isp_clock)) {
        dev_err(dev, "failed to get ISP clock info\n");
        return -ENODEV;
    }

    /* Register the UIO device */
    if ((err = uio_register_device(dev, uio_info))) {
        dev_err(dev, "failed to register UIO device for ISP\n");
        return err;
    }

    /* Register the uio_info with the platform data */
    uio_info->priv = priv;
    priv->uio_info[0] = uio_info;
    spin_lock_init(&priv->isp_lock);
    priv->isp_flags = 1;        /* ISP interrupt is active */

    /* Register the UIO device with the PM framework */
    pm_runtime_enable(dev);

    /* Enable clocks */
    if ((err = clk_prepare_enable(priv->isp_clock))) {
        dev_err(dev, "failed to enable clock\n");
        return -EBUSY;
    }

    /* Print some information */
    dev_dbg(dev, "ISP irq %ld clock %ld Hz\n", uio_info->irq, clk_get_rate(priv->isp_clock));

    return 0;
}

static int isp_open(struct uio_info *uio_info, __attribute__((unused)) struct inode *inode)
{
    struct uio_isp_platform_data *priv = uio_info->priv;

    dev_dbg(&priv->pdev->dev, "isp open\n");
    /* Wait until the PM has woken up the device */
    pm_runtime_get_sync(&priv->pdev->dev);

    if (!test_and_set_bit(0, &priv->isp_flags))
        enable_irq((unsigned int)uio_info->irq);

    return 0;
}

static int isp_close(struct uio_info *uio_info, __attribute__((unused)) struct inode *inode)
{
    struct uio_isp_platform_data *priv = uio_info->priv;

    dev_dbg(&priv->pdev->dev, "isp close\n");
    /* Tell the PM that the device has become idle */
    pm_runtime_put_sync(&priv->pdev->dev);

    if (test_and_clear_bit(0, &priv->isp_flags))
        disable_irq((unsigned int)uio_info->irq);

    return 0;
}

static irqreturn_t isp_irq_handler(int irq, struct uio_info *uio_info)
{
    struct uio_isp_platform_data *priv = uio_info->priv;
    u32 fstart_status;
    u32 fend_status;
    u32 stats_status;

    /* Mask all interrupts, umask by user process */
    write_reg(priv->base_addr, ISP_INT_FRAME_START_MASK, 0);
    write_reg(priv->base_addr, ISP_INT_FRAME_END_MASK, 0);
    write_reg(priv->base_addr, ISP_INT_STATS_MASK, 0);

    fstart_status = read_reg(priv->base_addr, ISP_INT_FRAME_START_STATUS);
    fend_status = read_reg(priv->base_addr, ISP_INT_FRAME_END_STATUS);
    stats_status = read_reg(priv->base_addr, ISP_INT_STATS_STATUS);

    if (!(fstart_status | fend_status | stats_status)) {
        return IRQ_NONE;
    }

    return IRQ_HANDLED;
}

static int irq_control(struct uio_info *uio_info, s32 irq_on)
{
    struct uio_isp_platform_data *const priv = uio_info->priv;
    unsigned long flag;

    if (uio_info == priv->uio_info[0]) {
        dev_dbg(&priv->pdev->dev, "ISP irq_control\n");
        /* This is the ISP */
        spin_lock_irqsave(&priv->isp_lock, flag);
        if (irq_on) {
            if (!test_and_set_bit(0, &priv->isp_flags))    {
                enable_irq((unsigned int)uio_info->irq);
            }
        }
        else {
            if (test_and_clear_bit(0, &priv->isp_flags)) {
                disable_irq((unsigned int)uio_info->irq);
            }
        }
        spin_unlock_irqrestore(&priv->isp_lock, flag);
    } else {
        dev_dbg(&priv->pdev->dev, "ISP Wrapper irq_control\n");
        /* This is the ISP Wrapper */
        spin_lock_irqsave(&priv->wrapper_lock, flag);
        if (irq_on) {
            if (!test_and_set_bit(0, &priv->wrapper_flags))    {
                enable_irq((unsigned int)uio_info->irq);
            }
        }
        else {
            if (test_and_clear_bit(0, &priv->wrapper_flags)) {
                disable_irq((unsigned int)uio_info->irq);
            }
        }
        spin_unlock_irqrestore(&priv->wrapper_lock, flag);
    }

    return 0;
}

static int uio_isp_probe(struct platform_device *pdev)
{
    struct uio_isp_platform_data *priv;
    int err;

    /* Allocate memory for the common housekeeping structure */
    priv = devm_kzalloc(&pdev->dev, sizeof(*priv), GFP_KERNEL);
    if (!priv)
        return -ENOMEM;
    priv->pdev = pdev;

    if ((err = isp_wrapper_probe(pdev, priv)))
        return err;
    if ((err = isp_probe(pdev, priv)))
        return err;

    platform_set_drvdata(pdev, priv);

    return 0;
}

static int uio_isp_remove(struct platform_device *pdev)
{
    struct uio_isp_platform_data *priv = platform_get_drvdata(pdev);

    uio_unregister_device(priv->uio_info[0]);
    uio_unregister_device(priv->uio_info[1]);

    clk_disable_unprepare(priv->isp_clock);
    pm_runtime_disable(&priv->pdev->dev);

    platform_set_drvdata(pdev, NULL);

    return 0;
}

/* Power management */
static int uio_runtime_isp_nop(__attribute__((unused)) struct device *dev)
{
    return 0;
};

static const struct dev_pm_ops uio_dev_pm_isp_ops =
{
    .runtime_suspend = &uio_runtime_isp_nop,
    .runtime_resume = &uio_runtime_isp_nop,
};

/* Compatible table */
static const struct of_device_id rcar_isp_dt_ids[] =
{
    { .compatible = "renesas,isp-r8a77970", .data = 0 },
    { .compatible = "renesas,isp-r8a77980", .data = 0 },
    {},
};
MODULE_DEVICE_TABLE(of, rcar_isp_dt_ids);

/* Platform driver structure */
static struct platform_driver uio_isp_platform_driver =
{
    .probe = &uio_isp_probe,
    .remove = &uio_isp_remove,
    .driver =
    {
        .name = DRIVER_NAME,
        .owner = THIS_MODULE,
        .pm = &uio_dev_pm_isp_ops,
        .of_match_table = of_match_ptr(rcar_isp_dt_ids)
    }
};
module_platform_driver(uio_isp_platform_driver);

MODULE_AUTHOR("Renesas Electronics Corporation");
MODULE_DESCRIPTION("Userspace I/O driver for ISP");
MODULE_LICENSE("Dual MIT/GPL");
MODULE_ALIAS("platform:" DRIVER_NAME);

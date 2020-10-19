/*************************************************************************/ /*
 IMP Driver (kernel module)

 Copyright (C) 2015 - 2017 Renesas Electronics Corporation

 License        Dual MIT/GPLv2

 The contents of this file are subject to the MIT license as set out below.

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 Alternatively, the contents of this file may be used under the terms of
 the GNU General Public License Version 2 ("GPL") in which case the provisions
 of GPL are applicable instead of those above.

 If you wish to allow use of your version of this file only under the terms of
 GPL, and not to allow others to use your version of this file under the terms
 of the MIT license, indicate your decision by deleting the provisions above
 and replace them with the notice and other provisions required by GPL as set
 out in the file called "GPL-COPYING" included in this distribution. If you do
 not delete the provisions above, a recipient may use your version of this file
 under the terms of either the MIT license or GPL.

 This License is also included in this distribution in the file called
 "MIT-COPYING".

 EXCEPT AS OTHERWISE STATED IN A NEGOTIATED AGREEMENT: (A) THE SOFTWARE IS
 PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 PURPOSE AND NONINFRINGEMENT; AND (B) IN NO EVENT SHALL THE AUTHORS OR
 COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 GPLv2:
 If you wish to use this file under the terms of GPL, following terms are
 effective.

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; version 2 of the License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/ /*************************************************************************/

#include <linux/platform_device.h>
#include <linux/uio_driver.h>
#include <linux/module.h>
#include <linux/interrupt.h>
#include <linux/pm_runtime.h>
#include <linux/slab.h>
#include <linux/io.h>
#include <linux/dma-mapping.h>
#include <linux/irq.h>
#include <linux/irqdomain.h>

#include <linux/of.h>
#include <linux/of_platform.h>
#include <linux/of_address.h>
#include <linux/of_irq.h>

#define DRIVER_NAME "uio_imp"
#define DRIVER_VER "0.0"

enum {
	LUIO_DEVICE_IMP,
	LUIO_DEVICE_IMPSC,
	LUIO_DEVICE_IMPDES,
	LUIO_DEVICE_IMRLSX,
	LUIO_DEVICE_IMRX,
	LUIO_DEVICE_MEM,
	LUIO_DEVICE_VSP,
	LUIO_DEVICE_IMPDMAC,
	LUIO_DEVICE_IMPPSC,
	LUIO_DEVICE_IMPCNN,
};

#define IMP_INTERNAL_REG_SIZE 0x1000
#define IMP_NUM_DIST_HWIRQ 32

static int bsize = 7 * 1024 * 1024;
module_param(bsize, uint, S_IRUGO);

static bool clear_int = 0;
module_param(clear_int, bool, S_IRUGO);

struct imp_dev_data {
	int dtype;
	irqreturn_t (*handler)(int irq, struct uio_info *dev_info);
	void (*sreset)(struct uio_info *info);
	const char *name;
};

struct uio_imp_platdata {
	struct uio_info *uioinfo;
	struct platform_device *pdev;
	const struct imp_dev_data *dev_data;

	struct irq_chip irq_chip;
	struct irq_domain *irq_domain;
	unsigned int domain_irq[IMP_NUM_DIST_HWIRQ];
	int max_hwirq;
};

static unsigned int ReadReg(struct uio_info *info,
			unsigned long reg_offs)
{
	return ioread32(info->mem[0].internal_addr + reg_offs);
}

static void WriteReg(struct uio_info *info,
			unsigned long reg_offs, u32 data)
{
	iowrite32(data, info->mem[0].internal_addr + reg_offs);
}

static irqreturn_t imp_handler(int irq, struct uio_info *dev_info)
{
	u32 stat = ReadReg(dev_info, 0x10);

	if (clear_int && (stat & 0x00000020)) {
		/* clear INT state */
		WriteReg(dev_info, 0x10, 0x00000020);
		return IRQ_NONE;
	}

	if (stat != 0) {
		/* mask all interrupts */
		u32 mask = ReadReg(dev_info, 0x14);
		WriteReg(dev_info, 0x14, mask & 0x0fffffff);

		return IRQ_HANDLED;
	} else {
		return IRQ_NONE;
	}
}

static void imp_sreset(struct uio_info *info)
{
	/* software reset */
	WriteReg(info, 0x08, 0x80000000);
	WriteReg(info, 0x08, 0x00000000);
	ReadReg(info, 0x08);

	/* 32bit addressing mode */
	WriteReg(info, 0x08, 0x00000001);

	/* mask all interrupts */
	WriteReg(info, 0x14, 0);
}

static irqreturn_t impsc_handler(int irq, struct uio_info *dev_info)
{
	u32 stat = ReadReg(dev_info, 0x14);

	if (clear_int && (stat & 0x00000004)) {
		/* clear INT state */
		WriteReg(dev_info, 0x18, 0x00000004);
		return IRQ_NONE;
	}

	if (stat != 0) {
		/* mask all interrupts */
		WriteReg(dev_info, 0x20, 0xffffffff);

		return IRQ_HANDLED;
	} else {
		return IRQ_NONE;
	}
}

static void impsc_sreset(struct uio_info *info)
{
	/* software reset */
	WriteReg(info, 0x08, 0x00000001);
	WriteReg(info, 0x08, 0x00000000);
	ReadReg(info, 0x08);

	/* mask all interrupts */
	WriteReg(info, 0x20, 0xffffffff);
}

static irqreturn_t impdmac_handler(int irq, struct uio_info *dev_info)
{
	u32 stat = ReadReg(dev_info, 0x08);

	if (clear_int && (stat & 0x00000040)) {
		/* clear INT state */
		WriteReg(dev_info, 0x0c, 0x00000040);
		return IRQ_NONE;
	}

	if (stat != 0) {
		/* mask all interrupts */
		WriteReg(dev_info, 0x14, 0xffffffff);

		return IRQ_HANDLED;
	} else {
		return IRQ_NONE;
	}
}

static void impdmac_sreset(struct uio_info *info)
{
	/* software reset */
	WriteReg(info, 0x04, 0x80000000);
	WriteReg(info, 0x04, 0x00000000);
	ReadReg(info, 0x04);

	/* mask all interrupts */
	WriteReg(info, 0x14, 0xffffffff);
}

static irqreturn_t impcnn_handler(int irq, struct uio_info *dev_info)
{
	u32 stat = ReadReg(dev_info, 0x10);

	if (clear_int && (stat & 0x00000004)) {
		/* clear INT state */
		WriteReg(dev_info, 0x18, 0x00000004);
		return IRQ_NONE;
	}

	if (stat != 0) {
		/* mask all interrupts */
		WriteReg(dev_info, 0x1c, 0xffffffff);

		return IRQ_HANDLED;
	} else {
		return IRQ_NONE;
	}
}

static void impcnn_sreset(struct uio_info *info)
{
	/* software reset */
	WriteReg(info, 0x08, 0x00000001);
	WriteReg(info, 0x08, 0x00000000);
	ReadReg(info, 0x08);

	/* mask all interrupts */
	WriteReg(info, 0x1c, 0xffffffff);
}

static irqreturn_t impdist_handler(int irq, struct uio_info *dev_info)
{
	unsigned int bit;
	unsigned long stat;
	struct uio_imp_platdata *priv = dev_info->priv;

	stat = ReadReg(dev_info, 0x100); /* sr */
	stat &= ~ReadReg(dev_info, 0x10c); /* imr */

	if (stat & 0x100)
		stat |= ReadReg(dev_info, 0x110); /* g0intsel */
	if (stat & 0x200)
		stat |= ReadReg(dev_info, 0x114); /* g1intsel */
	if (stat & 0x400)
		stat |= ReadReg(dev_info, 0x118); /* g2intsel */
	stat &= ~0x700;

	if (!stat)
		return IRQ_NONE;

	for_each_set_bit(bit, &stat, IMP_NUM_DIST_HWIRQ)
		generic_handle_irq(priv->domain_irq[bit]);

	return IRQ_HANDLED;
}

static irqreturn_t impdist2_handler(int irq, struct uio_info *dev_info)
{
	unsigned int bit;
	unsigned long stat;
	struct uio_imp_platdata *priv = dev_info->priv;

	stat = ReadReg(dev_info, 0x100); /* sr */
	stat &= ~ReadReg(dev_info, 0x10c); /* imr */

	if (stat & 0x20000000)
		stat |= ReadReg(dev_info, 0x110); /* g0intsel */
	if (stat & 0x40000000)
		stat |= ReadReg(dev_info, 0x114); /* g1intsel */
	if (stat & 0x80000000)
		stat |= ReadReg(dev_info, 0x118); /* g2intsel */
	stat &= ~0xe0000000;

	if (!stat)
		return IRQ_NONE;

	for_each_set_bit(bit, &stat, IMP_NUM_DIST_HWIRQ)
		generic_handle_irq(priv->domain_irq[bit]);

	return IRQ_HANDLED;
}

static void impdist_irq_enable(struct irq_data *d)
{
	struct uio_imp_platdata *priv = irq_data_get_irq_chip_data(d);
	int hw_irq = irqd_to_hwirq(d);
	unsigned int d_var;

	d_var = ReadReg(priv->uioinfo, 0x10c) & ~BIT(hw_irq);
	WriteReg(priv->uioinfo, 0x10c, d_var);
}

static void impdist_irq_disable(struct irq_data *d)
{
	struct uio_imp_platdata *priv = irq_data_get_irq_chip_data(d);
	int hw_irq = irqd_to_hwirq(d);
	unsigned int d_var;

	d_var = ReadReg(priv->uioinfo, 0x10c) | BIT(hw_irq);
	WriteReg(priv->uioinfo, 0x10c, d_var);
}

static int impdist_irq_domain_map(struct irq_domain *h, unsigned int virq,
			       irq_hw_number_t hw)
{
	struct uio_imp_platdata *priv = h->host_data;

	dev_dbg(&priv->pdev->dev, "impdist_irq_domain_map: virq=%d, hw=%d\n", virq, (int)hw);
	priv->domain_irq[hw] = virq;
	irq_set_chip_data(virq, priv);
	irq_set_chip_and_handler(virq, &priv->irq_chip, handle_simple_irq);
	return 0;
}

static struct irq_domain_ops impdist_irq_domain_ops = {
	.map	= impdist_irq_domain_map,
};

static void impdist_sreset(struct uio_info *info)
{
	WriteReg(info, 0x500, 0);
}

static const struct imp_dev_data imp_dev_data_legacy = {
	.dtype = LUIO_DEVICE_IMP,
	.handler = imp_handler,
	.sreset = imp_sreset,
};
static const struct imp_dev_data imp_dev_data_shader = {
	.dtype = LUIO_DEVICE_IMPSC,
	.handler = impsc_handler,
	.sreset = impsc_sreset,
};
static const struct imp_dev_data imp_dev_data_distributer = {
	.dtype = LUIO_DEVICE_IMPDES,
	.handler = impdist_handler,
	.sreset = impdist_sreset,
};
static const struct imp_dev_data imp_dev_data_memory = {
	.dtype = LUIO_DEVICE_MEM,
};
static const struct imp_dev_data imp_dev_data_dmac = {
	.dtype = LUIO_DEVICE_IMPDMAC,
	.handler = impdmac_handler,
	.sreset = impdmac_sreset,
};
static const struct imp_dev_data imp_dev_data_distributer2 = {
	.dtype = LUIO_DEVICE_IMPDES,
	.handler = impdist2_handler,
};
static const struct imp_dev_data imp_dev_data_psc = {
	.dtype = LUIO_DEVICE_IMPPSC,
	.handler = impdmac_handler,	/* same as dmac */
	.sreset = impdmac_sreset,	/* same as dmac */
};
static const struct imp_dev_data imp_dev_data_cnn = {
	.dtype = LUIO_DEVICE_IMPCNN,
	.handler = impcnn_handler,
	.sreset = impcnn_sreset,
};

static const struct of_device_id of_imp_match[] = {
	{ .compatible = "renesas,impx4-legacy",       .data = &imp_dev_data_legacy       },
	{ .compatible = "renesas,impx4-shader",       .data = &imp_dev_data_shader       },
	{ .compatible = "renesas,impx4-distributer",  .data = &imp_dev_data_distributer  },
	{ .compatible = "renesas,impx4-memory",       .data = &imp_dev_data_memory       },
	{ .compatible = "renesas,impx5-dmac",         .data = &imp_dev_data_dmac         },
	{ .compatible = "renesas,impx5+-distributer", .data = &imp_dev_data_distributer2 },
	{ .compatible = "renesas,impx5+-psc",         .data = &imp_dev_data_psc          },
	{ .compatible = "renesas,impx5+-cnn",         .data = &imp_dev_data_cnn          },
	{ /* Terminator */ },
};
MODULE_DEVICE_TABLE(of, of_imp_match);

static int uio_imp_open(struct uio_info *info, struct inode *inode)
{
	struct uio_imp_platdata *priv = info->priv;

	/* software reset */
	if (priv->dev_data->sreset)
		priv->dev_data->sreset(info);

	return 0;
}

static int uio_imp_release(struct uio_info *info, struct inode *inode)
{
	return 0;
}

static int uio_imp_probe(struct platform_device *pdev)
{
	struct uio_info *uioinfo;
	struct uio_imp_platdata *priv;
	const struct imp_dev_data *dev_data;
	struct resource *res;
	struct uio_mem *uiomem;
	int ret = -EINVAL;

	/* alloc uioinfo for one device */
	uioinfo = devm_kzalloc(&pdev->dev, sizeof(*uioinfo),
			       GFP_KERNEL);
	if (!uioinfo) {
		dev_err(&pdev->dev, "unable to kmalloc\n");
		return -ENOMEM;
	}

	if (pdev->dev.of_node) {
		const struct of_device_id *match;
		match = of_match_node(of_imp_match, pdev->dev.of_node);
		if (!match)
			return ret;

		dev_data = match->data;
		uioinfo->name = pdev->dev.of_node->name;
		uioinfo->version = DRIVER_VER;
	} else {
		dev_data = pdev->dev.platform_data;
		uioinfo->name = dev_data->name;
		uioinfo->version = DRIVER_VER;
	}

	if (!dev_data) {
		dev_err(&pdev->dev, "missing platform_data\n");
		return ret;
	}

	priv = devm_kzalloc(&pdev->dev, sizeof(*priv), GFP_KERNEL);
	if (!priv) {
		dev_err(&pdev->dev, "unable to kmalloc\n");
		return -ENOMEM;
	}

	priv->uioinfo = uioinfo;
	priv->pdev = pdev;
	priv->dev_data = dev_data;

	/* MEM */
	uiomem = &uioinfo->mem[0];
	res = platform_get_resource(pdev, IORESOURCE_MEM, 0);

	if (res) {
		uiomem->memtype = UIO_MEM_PHYS;
		uiomem->addr = res->start;
		uiomem->size = resource_size(res);
		uiomem->name = res->name;

		/* If reg[1] is shown, use it as internnal addr.
		   Otherwise, use reg[0]. */
		res = platform_get_resource(pdev, IORESOURCE_MEM, 1);
		if (res)
			uiomem->internal_addr = devm_ioremap_nocache(&pdev->dev,
					res->start, resource_size(res));
		else
			uiomem->internal_addr = devm_ioremap_nocache(&pdev->dev,
					uiomem->addr, IMP_INTERNAL_REG_SIZE);

		if (!uiomem->internal_addr) {
			dev_err(&pdev->dev, "unable to ioremap\n");
			return -ENOMEM;
		}
	} else if (dev_data->dtype == LUIO_DEVICE_MEM) {
		/* allocate physically-contiguous memory */
		int buffer_size = bsize;
		dev_info(&pdev->dev, "Allocating Buffer size = %d\n", buffer_size);
		uiomem->memtype = UIO_MEM_PHYS;
		uiomem->size = buffer_size;
		uiomem->name = "mem";

		uiomem->internal_addr = dmam_alloc_coherent(&pdev->dev,
				buffer_size, &uiomem->addr, GFP_KERNEL);
		if (!uiomem->internal_addr) {
			dev_err(&pdev->dev, "unable to dma_alloc_coherent\n");
			return -ENOMEM;
		}
	} else {
		dev_err(&pdev->dev, "missing memory map\n");
		return -EINVAL;
	}

	/* IRQ */
	if (dev_data->handler) {
		if (pdev->dev.of_node)
			uioinfo->irq = irq_of_parse_and_map(pdev->dev.of_node, 0);
		else {
			ret = platform_get_irq(pdev, 0);
			if (ret >= 0)
				uioinfo->irq = ret;
			else if (ret != -ENXIO) {
				dev_err(&pdev->dev, "failed to get IRQ\n");
				return ret;
			}
		}
		if (uioinfo->irq) {
			uioinfo->handler = dev_data->handler;
			if (dev_data->dtype != LUIO_DEVICE_IMPDES)
				uioinfo->irq_flags = IRQF_SHARED;
		}
	}
	dev_dbg(&pdev->dev, "irq = %d\n", (int)uioinfo->irq);

	/* irq demuxing on distributer handler */
	if (uioinfo->irq && dev_data->dtype == LUIO_DEVICE_IMPDES) {
		struct irq_chip *irq_chip = &priv->irq_chip;
		irq_chip->name = uioinfo->name;
		irq_chip->irq_mask = impdist_irq_disable;
		irq_chip->irq_unmask = impdist_irq_enable;
		irq_chip->irq_enable = impdist_irq_enable;
		irq_chip->irq_disable = impdist_irq_disable;
		irq_chip->flags = IRQCHIP_SKIP_SET_WAKE;

		priv->irq_domain = irq_domain_add_simple(pdev->dev.of_node,
					IMP_NUM_DIST_HWIRQ, 0,
					&impdist_irq_domain_ops, priv);
		if (!priv->irq_domain) {
			dev_err(&pdev->dev, "cannot initialize irq domain\n");
			ret = -ENXIO;
			goto err1;
		}
	}

	pm_runtime_enable(&pdev->dev);
	pm_runtime_get_sync(&priv->pdev->dev);

#ifdef CFG_USE_FPGA
		dev_info(&pdev->dev, "%s(0x%08lx)\n",
				uioinfo->name, (unsigned long)uiomem->addr);
#else
	/* software reset */
	if (dev_data->sreset)
		dev_data->sreset(uioinfo);

	/* print hardware version */
	if (uiomem->internal_addr && dev_data->dtype != LUIO_DEVICE_MEM) {
		unsigned int vcr0 = ReadReg(uioinfo, 0x0);
		unsigned int vcr1 = ReadReg(uioinfo, 0x4);
		dev_info(&pdev->dev, "%s(0x%08lx): VCR = 0x%08x, 0x%08x\n",
				uioinfo->name, (unsigned long)uiomem->addr, vcr0, vcr1);
	}
#endif /* CFG_USE_FPGA */

	uioinfo->open = uio_imp_open;
	uioinfo->release = uio_imp_release;
	uioinfo->priv = priv;

	ret = uio_register_device(&pdev->dev, priv->uioinfo);
	if (ret) {
		dev_err(&pdev->dev, "unable to register uio device\n");
		goto err2;
	}

	platform_set_drvdata(pdev, priv);
	return 0;

err2:
	pm_runtime_put_sync(&priv->pdev->dev);
	pm_runtime_disable(&pdev->dev);
	if (priv->irq_domain)
		irq_domain_remove(priv->irq_domain);

err1:
	if (pdev->dev.of_node && uioinfo->irq)
		irq_dispose_mapping(uioinfo->irq);

	return ret;
}

static int uio_imp_remove(struct platform_device *pdev)
{
	struct uio_imp_platdata *priv = platform_get_drvdata(pdev);

	uio_unregister_device(priv->uioinfo);
	pm_runtime_put_sync(&priv->pdev->dev);
	pm_runtime_disable(&pdev->dev);

	if (priv->irq_domain)
		irq_domain_remove(priv->irq_domain);

	if (pdev->dev.of_node && priv->uioinfo->irq)
		irq_dispose_mapping(priv->uioinfo->irq);

	return 0;
}

static int uio_imp_runtime_nop(struct device *dev)
{
	return 0;
}

static const struct dev_pm_ops uio_imp_dev_pm_ops = {
	.runtime_suspend = uio_imp_runtime_nop,
	.runtime_resume = uio_imp_runtime_nop,
};

static struct platform_driver uio_imp = {
	.probe = uio_imp_probe,
	.remove = uio_imp_remove,
	.driver = {
		.name = DRIVER_NAME,
		.pm = &uio_imp_dev_pm_ops,
		.of_match_table = of_match_ptr(of_imp_match),
	},
};

module_platform_driver(uio_imp);

MODULE_AUTHOR("RenesasElectronicsCorp.");
MODULE_DESCRIPTION("Userspace I/O driver for IMP");
MODULE_LICENSE("GPL v2");

require recipes-kernel/linux/linux-agl-4.19.inc

ENABLE_UART_raspberrypi4 = "1"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"


# For Xen

SRC_URI_append =" \
    ${@bb.utils.contains('AGL_XEN_WANTED','1','file://0002-Disable-DMA-in-sdhci-driver.patch','',d)} \
"

SRC_URI_append =" \
    ${@bb.utils.contains('AGL_XEN_WANTED','1','file://0003-Fix-PCIe-in-dom0-for-RPi4.patch','',d)} \
"

SRC_URI_append = " \
    ${@bb.utils.contains('AGL_XEN_WANTED','1','file://xen-be.cfg','',d)} \
"

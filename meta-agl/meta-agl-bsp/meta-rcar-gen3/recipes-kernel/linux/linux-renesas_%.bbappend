FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

require recipes-kernel/linux/linux-agl.inc

SRC_URI_append  = " file://namespace_fix.cfg \
    "

# Add ADSP patch to enable and add sound hardware abstraction
SRC_URI_append = " \
    file://0004-ADSP-enable-and-add-sound-hardware-abstraction.patch \
"

# Gcc 10 build fix
SRC_URI_append = " \
    file://0001-scripts-dtc-Remove-redundant-YYLOC-global-declaratio.patch \
"


# For Xen
SRC_URI_append = " \
    ${@bb.utils.contains('AGL_XEN_WANTED','1','file://xen-be.cfg','',d)} \
"
SRC_URI_append_m3ulcb = " \
    ${@bb.utils.contains('AGL_XEN_WANTED','1','file://r8a7796-m3ulcb-xen.dts;subdir=git/arch/${ARCH}/boot/dts/renesas','',d)} \
"
KERNEL_DEVICETREE_append_m3ulcb = " \
    ${@bb.utils.contains('AGL_XEN_WANTED','1','renesas/r8a7796-m3ulcb-xen.dtb','',d)} \
"

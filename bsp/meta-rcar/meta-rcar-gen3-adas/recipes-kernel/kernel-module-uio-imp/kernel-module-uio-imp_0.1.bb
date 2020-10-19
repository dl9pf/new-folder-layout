SUMMARY = "IMP Primitive Driver Module"
LICENSE = "GPLv2 & MIT"
LIC_FILES_CHKSUM = " \
    file://GPL-COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://MIT-COPYING;md5=fea016ce2bdf2ec10080f69e9381d378 \
    "

inherit module

PR = "r0"
PV = "0.1"

SRC_URI = " \
    file://Makefile \
    file://uio_imp.c \
    file://GPL-COPYING \
    file://MIT-COPYING \
    "

S = "${WORKDIR}"

KERNEL_MODULE_AUTOLOAD += "uio_imp"
KERNEL_MODULE_PROBECONF += "uio_imp"

module_conf_uio_imp = ""
module_conf_uio_imp_r8a7795 = "options uio_imp clear_int"
module_conf_uio_imp_r8a7796 = "options uio_imp clear_int"

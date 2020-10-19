SUMMARY = "Cache Memory Primitive Module"
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
    file://cmemdrv.c \
    file://GPL-COPYING \
    file://MIT-COPYING \
    "

S = "${WORKDIR}"

KERNEL_MODULE_AUTOLOAD += "cmemdrv"
KERNEL_MODULE_PROBECONF += "cmemdrv"
module_conf_cmemdrv = "options cmemdrv bsize=0x7000000"

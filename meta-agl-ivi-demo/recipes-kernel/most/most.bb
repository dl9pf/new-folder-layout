DESCRIPTION = "Build MOST driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module

PV = "0.1"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/most;protocol=https;branch=${AGL_BRANCH}"

S = "${WORKDIR}/git/driver"
SRCREV = "e4dbbaf9e7652efaed0df3e0aab4464f5f228573"

KERNEL_MODULE_AUTOLOAD += "aim_cdev aim_sound aim_network aim_v4l2 hdm_i2c hdm_dim2 hdm_usb mostcore"

SUMMARY     = "AGL HTML5 Background Application"
LICENSE     = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

PV      = "1.0+git${SRCPV}"
S       = "${WORKDIR}/git/"

SRC_URI = "git://github.com/AGL-web-applications/background.git;protocol=https;branch=master"
SRCREV = "3b8dae349d428c0230b9885f86d421d43cda5638"

DEPENDS = "nodejs-native"

inherit aglwgt

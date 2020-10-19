SUMMARY     = "Low level CAN generator"
DESCRIPTION = "Generator used to customize low level CAN service with customs signals"
SECTION     = "devel"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit cmake pkgconfig
BBCLASSEXTEND = "nativesdk"
DEPENDS = " cmake-apps-module"

SRC_URI = "gitsm://gerrit.automotivelinux.org/gerrit/src/low-level-can-generator;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "0a3e354c3d81866e1a755367ab5592b3ced868bb"

PV = "${AGLVERSION}"
S  = "${WORKDIR}/git"


SUMMARY     = "AGL Qt AppFW Library"
DESCRIPTION = "libqtappfw"
HOMEPAGE    = "http://docs.automotivelinux.org"
LICENSE     = "Apache-2.0"
SECTION     = "libs"

BBCLASSEXTEND = "nativesdk"

LIC_FILES_CHKSUM = "file://LICENSE;md5=ae6497158920d9524cf208c09cc4c984"

DEPENDS += "qtbase qtdeclarative qtwebsockets"

inherit cmake_qt5

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/libqtappfw;protocol=https;branch=${AGL_BRANCH}"
SRCREV  = "fe20f1b029f67dee1f790ade7a9114086f2abd38"
S       = "${WORKDIR}/git/"

# PV needs to be modified with SRCPV to work AUTOREV correctly
PV = "0.0+git${SRCPV}"

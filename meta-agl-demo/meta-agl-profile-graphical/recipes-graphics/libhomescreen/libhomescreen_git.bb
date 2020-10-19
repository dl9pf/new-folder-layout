SUMMARY     = "AGL Home Screen Library"
DESCRIPTION = "libhomescreen"
HOMEPAGE    = "http://docs.automotivelinux.org"
LICENSE     = "Apache-2.0"
SECTION     = "libs"

BBCLASSEXTEND = " nativesdk"

LIC_FILES_CHKSUM = "file://LICENSE;md5=ae6497158920d9524cf208c09cc4c984"

DEPENDS = "af-binder json-c"

inherit cmake

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/libhomescreen.git;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "0d65d54ba63508c0ef545d02e94d5702f9c8ecb3"
S = "${WORKDIR}/git"

RDEPENDS_${PN} = "agl-service-homescreen"

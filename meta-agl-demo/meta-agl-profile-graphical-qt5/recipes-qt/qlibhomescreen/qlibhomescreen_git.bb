SUMMARY     = "A wrapper library of libhomescreen for Qt Application in AGL"
SECTION     = "libs"
LICENSE     = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2ee41112a44fe7014dce33e26468ba93"

DEPENDS = "qtbase libhomescreen"
RDEPENDS_${PN} = "libhomescreen"

inherit qmake5

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/libqthomescreen.git;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "b218476402bceda7eb42d41064552a7261ff3205"
S = "${WORKDIR}/git"

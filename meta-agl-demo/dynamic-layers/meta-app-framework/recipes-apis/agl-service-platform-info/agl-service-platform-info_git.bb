SUMMARY     = "Platform info provider binding"
DESCRIPTION = "AGL Platform info provider binding"
HOMEPAGE    = "https://git.automotivelinux.org/apps/agl-service-platform-info/"
SECTION     = "apps"

LICENSE     = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/agl-service-platform-info;protocol=https;branch=${AGL_BRANCH}"
SRCREV  = "${AGL_APP_REVISION}"

PV = "1.0+git${SRCPV}"
S  = "${WORKDIR}/git"

DEPENDS = "libafb-helpers"

inherit cmake aglwgt pkgconfig

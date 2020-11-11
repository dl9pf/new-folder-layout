SUMMARY     = "Navigation Service Binding"
DESCRIPTION = "AGL Navigation Service API Binding"
SECTION     = "apps"

LICENSE     = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ae6497158920d9524cf208c09cc4c984"

DEPENDS = "json-c libdbus-c++"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/agl-service-navigation;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "${AGL_APP_REVISION}"

PV = "0.1+git${SRCPV}"
S  = "${WORKDIR}/git"

inherit cmake aglwgt pkgconfig

RDEPENDS_${PN} += "json-c libdbus-c++"

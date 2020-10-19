SUMMARY     = "HVAC Service Binding"
DESCRIPTION = "AGL HVAC Service Binding"
HOMEPAGE    = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/apps/agl-service-hvac"
SECTION     = "apps"

LICENSE     = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ae6497158920d9524cf208c09cc4c984"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/agl-service-hvac;protocol=https;branch=${AGL_BRANCH}"
SRCREV  = "${AGL_APP_REVISION}"

PV = "1.0+git${SRCPV}"
S  = "${WORKDIR}/git"

DEPENDS = "json-c"

inherit cmake aglwgt pkgconfig

RDEPENDS_${PN} += "agl-service-identity-agent agl-service-can-low-level"
RRECOMMENDS_${PN} += "agl-service-hvac-conf"

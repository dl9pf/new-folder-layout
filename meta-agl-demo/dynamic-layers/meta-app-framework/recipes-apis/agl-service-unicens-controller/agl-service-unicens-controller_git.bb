SUMMARY     = "Infotainment network device control"
DESCRIPTION = "Abstraction layer to control INICnet devices"
HOMEPAGE    = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/apps/agl-service-unicens-controller"
SECTION     = "apps"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRC_URI = "gitsm://gerrit.automotivelinux.org/gerrit/apps/agl-service-unicens-controller;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "${AGL_APP_REVISION}"

PV = "0.1+git${SRCPV}"
S  = "${WORKDIR}/git"

inherit cmake aglwgt pkgconfig

DEPENDS += "json-c af-binder libafb-helpers"
RDEPENDS_${PN} += "agl-service-unicens"


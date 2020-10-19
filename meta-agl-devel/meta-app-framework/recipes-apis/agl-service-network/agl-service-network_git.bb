SUMMARY     = "Network Service Binding"
DESCRIPTION = "AGL Network Service Binding"
HOMEPAGE    = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/apps/agl-service-network"
SECTION     = "apps"

LICENSE     = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ae6497158920d9524cf208c09cc4c984"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/agl-service-network;protocol=https;branch=${AGL_BRANCH}"
SRCREV  = "${AGL_APP_REVISION}"

PV = "1.0+git${SRCPV}"
S  = "${WORKDIR}/git"

DEPENDS = "glib-2.0 json-c"

inherit cmake aglwgt pkgconfig

do_install_append() {
    install -d ${D}${sbindir}
    install -m 755 ${B}/build-release/test/agl-service-network-ctl ${D}${sbindir}
}

FILES_${PN}-tools = "${sbindir}/agl-service-network-ctl"
PACKAGES_prepend = "${PN}-tools "

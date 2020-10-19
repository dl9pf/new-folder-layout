SUMMARY     = "Bluetooth MAP Service Binding"
DESCRIPTION = "AGL Bluetooth MAP Service Binding"
HOMEPAGE    = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/apps/agl-service-bluetooth-map"
SECTION     = "apps"

LICENSE     = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/agl-service-bluetooth-map;protocol=https;branch=${AGL_BRANCH}"
SRCREV  = "${AGL_APP_REVISION}"

PV = "1.0+git${SRCPV}"
S  = "${WORKDIR}/git"

DEPENDS = "glib-2.0 json-c"
RDEPENDS_${PN} = "bluez5-obex agl-service-bluetooth"

inherit cmake aglwgt pkgconfig

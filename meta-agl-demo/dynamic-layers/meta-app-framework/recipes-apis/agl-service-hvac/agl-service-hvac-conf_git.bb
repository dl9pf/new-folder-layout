SUMMARY     = "HVAC Service Binding Configuration"
DESCRIPTION = "AGL HVAC Service Binding Configuration"
HOMEPAGE    = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/apps/agl-service-hvac"
SECTION     = "apps"

LICENSE     = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ae6497158920d9524cf208c09cc4c984"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/agl-service-hvac;protocol=https;branch=${AGL_BRANCH}"
SRCREV  = "${AGL_APP_REVISION}"

PV = "1.0+git${SRCPV}"
S  = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    install -D -m 644 ${S}/hvac.json ${D}${sysconfdir}/hvac.json
}

do_install_append_ulcb() {
    sed -i -e "s#vcan0#sllin0#g" ${D}${sysconfdir}/hvac.json
}


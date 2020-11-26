SUMMARY = "Binding embedding test framework to test others binding"
DESCRIPTION = "This make testing binding running with Application Framework binder \
easier by simply test verb return as well as event reception."
HOMEPAGE = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/apps/app-afb-test"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"
SECTION = "apps"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/app-afb-test;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "${AGL_APP_REVISION}"

DEPENDS += "lua libafb-helpers libappcontroller"
RDEPENDS_${PN} += "lua bash jq"
RDEPENDS_${PN}-ptest += "af-binder"

PV = "${AGLVERSION}"
S  = "${WORKDIR}/git"

inherit cmake aglwgt pkgconfig ptest

do_install_append() {
       install -d ${D}${bindir}
       install -m 775 ${S}/afm-test.target.sh ${D}${bindir}/afm-test
}


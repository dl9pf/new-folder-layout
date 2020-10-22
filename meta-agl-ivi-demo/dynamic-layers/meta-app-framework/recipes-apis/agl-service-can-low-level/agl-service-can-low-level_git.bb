SUMMARY     = "Low level CAN service"
DESCRIPTION = "AGL Service application for read and decode CAN messages"
HOMEPAGE    = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/apps/low-level-can-service"
SECTION     = "apps"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/agl-service-can-low-level;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "${AGL_APP_REVISION}"

PV = "${AGLVERSION}"
S  = "${WORKDIR}/git"

DEPENDS = "libafb-helpers libappcontroller"

inherit cmake aglwgt pkgconfig ptest

# For now, just enable J1939 on the qemu platforms where we know the
# linux-yocto kernel is new enough (>= 5.4) and has the support enabled.
AGLWGT_CMAKE_CONFIGURE_ARGS_append_qemuall = " -DWITH_FEATURE_J1939=ON"

RDEPENDS_${PN} = "virtual/low-can-dev-mapping"

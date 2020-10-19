SUMMARY = "AFB helpers library"
DESCRIPTION = "AFB helpers library to ease JSON object manipulation and binding interaction"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/libafb-helpers;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "1d1c6cef6039effd4c045a76d30414b589336d0e"

PV = "${AGLVERSION}"
S  = "${WORKDIR}/git"

DEPENDS_append = " af-binder jq"


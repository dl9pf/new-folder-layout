SUMMARY = "CMake module to ease development of apps"
DESCRIPTION = "This is a migration of former app-templates git submodule which let you \
ease the development of apps and widget building."
HOMEPAGE = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/src/cmake-apps-module"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "apps"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/cmake-apps-module;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "7c03348d9c2de43684fa69c3c9e227b25daceaaa"

PV = "${AGLVERSION}"
S  = "${WORKDIR}/git"

inherit cmake

FILES_${PN} += " ${datadir}/*/Modules/CMakeAfbTemplates*"

BBCLASSEXTEND = "native nativesdk"


SUMMARY     = "homescreen-demo-ci AGL client shell for testing in CI"
DESCRIPTION = "homescreen-demo-ci AGL client shell for testing in CI"
HOMEPAGE    = "http://docs.automotivelinux.org"
LICENSE     = "MIT"
SECTION     = "apps"
LIC_FILES_CHKSUM = "file://COPYING;md5=374fee6a7817f1e1a5a7bfb7b7989553"

DEPENDS = "\
        qtbase \
        qtdeclarative \
        qtquickcontrols2 \
        agl-service-homescreen \
        agl-service-weather \
        libqtappfw \
        libhomescreen \
        libafb-helpers-qt \
        wayland-native \
        wayland \
        qtwayland \
        qtwayland-native \
"

inherit qmake5 systemd pkgconfig aglwgt

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/homescreen-demo-ci;protocol=https;branch=${AGL_BRANCH}"
SRCREV  = "${AGL_APP_REVISION}"

PV      = "1.0+git${SRCPV}"
S       = "${WORKDIR}/git/"

PATH_prepend = "${STAGING_DIR_NATIVE}${OE_QMAKE_PATH_QT_BINS}:"

OE_QMAKE_CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'agl-devel', '' , '-DQT_NO_DEBUG_OUTPUT', d)}"

RDEPENDS_${PN} += " \
        libqtappfw \
"

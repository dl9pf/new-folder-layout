SUMMARY     = "Session / Policy Manager for PipeWire"
HOMEPAGE    = "https://gitlab.freedesktop.org/pipewire/wireplumber"
BUGTRACKER  = "https://gitlab.freedesktop.org/pipewire/wireplumber/issues"
AUTHOR      = "George Kiagiadakis <george.kiagiadakis@collabora.com>"
SECTION     = "multimedia"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;beginline=3;md5=e8ad01a5182f2c1b3a2640e9ea268264"

inherit meson pkgconfig gobject-introspection

DEPENDS = "glib-2.0 glib-2.0-native pipewire"

SRC_URI = "\
    git://gitlab.freedesktop.org/pipewire/wireplumber.git;protocol=https;branch=master \
    https://raw.githubusercontent.com/skystrife/cpptoml/fededad7169e538ca47e11a9ee9251bc361a9a65/include/cpptoml.h \
    file://0001-Build-cpptoml-without-a-cmake-subproject.patch \
"
SRCREV = "0e98e4150b73d0bed9b72bf8d3eba49671962991"
SRC_URI[sha256sum] = "3e4e1d315fa1229921c7a4297ead08775b5bb1220c18a7eac62db9ca7e79df0d"

PV = "0.1.90+git${SRCPV}"
S  = "${WORKDIR}/git"

do_configure_prepend() {
    mkdir -p ${WORKDIR}/git/subprojects/cpptoml/include
    cp -f ${WORKDIR}/cpptoml.h ${WORKDIR}/git/subprojects/cpptoml/include/
}

PACKAGES =+ "${PN}-config"

FILES_${PN} += "\
    ${libdir}/wireplumber-*/* \
"
RPROVIDES_${PN} += "virtual/pipewire-sessionmanager"
RDEPENDS_${PN} += "virtual/wireplumber-config"


FILES_${PN}-config += "\
    ${sysconfdir}/wireplumber/* \
"
CONFFILES_${PN}-config += "\
    ${sysconfdir}/wireplumber/* \
"

RPROVIDES_${PN}-config += "virtual/wireplumber-config"

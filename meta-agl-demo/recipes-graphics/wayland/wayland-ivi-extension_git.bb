SUMMARY = "Wayland IVI Extension"
DESCRIPTION = "GENIVI Layer Management API based on Wayland IVI Extension"
HOMEPAGE = "http://projects.genivi.org/wayland-ivi-extension"
BUGTRACKER = "http://bugs.genivi.org/enter_bug.cgi?product=Wayland%20IVI%20Extension"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1f1a56bb2dadf5f2be8eb342acf4ed79"

SRC_URI = "git://github.com/GENIVI/${BPN}.git;protocol=http \
           file://0001-Added-ivi-id-agent-to-CMake.patch \
           file://0002-ivi-id-agent-added-ivi-id-agent.patch \
           file://0003-ivi-controller-load-id-agent-module.patch \
           file://0002-add-LayerManagerControl-error-status.patch \
           file://0004-ivi-ilmcontrol-added-focus-notification.patch \
           file://0005-disable-EGLWLMockNavigation-example-build.patch \
           file://0006-fix-plugin-registry-include.patch \
"
SRC_URI_append_wandboard = " file://wandboard_fix_build.patch"
SRCREV = "736fb654ac81230cf4f9e51a5772d3a02d7639bf"

PV = "2.2.0+git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS = "weston virtual/libgles2 pixman wayland-native"

FILESEXTRAPATHS_prepend := ":${THISDIR}/wayland-ivi-extension:"

inherit cmake

EXTRA_OECMAKE := "-DWITH_ILM_INPUT=1"

FILES_${PN} += "${libdir}/weston/*"
FILES_${PN} += "${datadir}/wayland-protocols/stable/ivi-application/*"

FILES_${PN}-dbg += "${libdir}/weston/.debug/*"

EXTRA_OECMAKE += "-DLIB_SUFFIX=${@d.getVar('baselib', True).replace('lib', '')}"

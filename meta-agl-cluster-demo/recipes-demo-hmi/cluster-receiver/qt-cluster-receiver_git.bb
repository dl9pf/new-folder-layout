SUMMARY     = "Instrument Cluster Receiver AGL Demonstration"
DESCRIPTION = "AGL HMI Application for demonstrating instrument cluster remote display"
HOMEPAGE    = "https://gerrit.automotivelinux.org/gerrit/admin/repos/apps/agl-qt-cluster-demo-receiver"
SECTION     = "apps"

LICENSE     = "Apache-2.0 & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ae6497158920d9524cf208c09cc4c984 \
                    file://app/surface.hpp;beginline=5;endline=21;md5=5351c531a191f0e3463aafcd0a6a00a3"

SRC_URI = "gitsm://gerrit.automotivelinux.org/gerrit/apps/agl-qt-cluster-demo-receiver;protocol=https;branch=${AGL_BRANCH}"
SRCREV  = "${AGL_APP_REVISION}"

PV = "1.0+git${SRCPV}"
S  = "${WORKDIR}/git"

# build-time dependencies
DEPENDS += "wayland wayland-native \
	    qtwayland qtwayland-native \
	    qtquickcontrols2 qtwebsockets qtbase qtdeclarative \
	    gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad"

inherit cmake_qt5 pkgconfig aglwgt

RDEPENDS_${PN} += " \
	gstreamer1.0-plugins-base \
	gstreamer1.0-plugins-good \
	gstreamer1.0-plugins-bad \
"

SUMMARY     = "Audio Mixer Service Binding"
DESCRIPTION = "AGL Audio Mixer Service Binding"
SECTION     = "apps"
LICENSE     = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;beginline=3;md5=e8ad01a5182f2c1b3a2640e9ea268264"

PV = "0.1+git${SRCPV}"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/agl-service-audiomixer.git;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "${AGL_APP_REVISION}"

S  = "${WORKDIR}/git"

inherit cmake aglwgt pkgconfig

DEPENDS += "pipewire wireplumber json-c"
RDEPENDS_${PN} = "agl-service-signal-composer"

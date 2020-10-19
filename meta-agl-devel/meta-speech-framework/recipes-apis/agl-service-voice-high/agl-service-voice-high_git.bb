SUMMARY = "High level voice service binding"
DESCRIPTION = "agl-service-voice-high is the binding library"
HOMEPAGE = "https://gerrit.automotivelinux.org/gerrit/apps/agl-service-voice-high"
SECTION = "apps"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS = "json-c systemd af-binder libafb-helpers libappcontroller nlohmann-json glib-2.0"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/agl-service-voice-high;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "${AGL_APP_REVISION}"

PV = "1.0+git${SRCPV}"
S = "${WORKDIR}/git"

inherit cmake aglwgt

RDEPENDS_${PN} += "virtual/voice-high-config"

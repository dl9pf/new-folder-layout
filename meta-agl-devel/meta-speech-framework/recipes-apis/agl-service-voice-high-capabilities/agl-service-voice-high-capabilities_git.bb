SUMMARY = "agl-service-voice-high"
DESCRIPTION = "AGL High Level Voice service"
HOMEPAGE = "https://git.automotivelinux.org/apps/agl-service-voice-high-capabilities"
SECTION = "apps"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS = "libafb-helpers libappcontroller nlohmann-json"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/agl-service-voice-high-capabilities.git;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "${AGL_APP_REVISION}"

PV = "0.1+git${SRCPV}"
S = "${WORKDIR}/git"

inherit cmake pkgconfig aglwgt

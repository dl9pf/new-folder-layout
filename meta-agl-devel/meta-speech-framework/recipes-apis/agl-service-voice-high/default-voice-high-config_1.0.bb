SUMMARY = "High level voice service binding default voiceagent configuration"
DESCRIPTION = "Default voiceagent JSON configuration file for agl-service-voice-high binding"
HOMEPAGE = "https://gerrit.automotivelinux.org/gerrit/apps/agl-service-voice-high"
SECTION = "apps"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "file://voice-high.json"

inherit allarch

do_compile[noexec] = "1"

do_install () {
    install -D -m 644 ${WORKDIR}/voice-high.json ${D}${sysconfdir}/xdg/AGL/voice-high.json
}

RPROVIDES_${PN} += "virtual/voice-high-config"


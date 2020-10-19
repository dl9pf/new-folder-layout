SUMMARY     = "AGL configuration file for wireplumber"
HOMEPAGE    = "https://gitlab.freedesktop.org/gkiagia/wireplumber"
BUGTRACKER  = "https://jira.automotivelinux.org"
AUTHOR      = "George Kiagiadakis <george.kiagiadakis@collabora.com>"
SECTION     = "multimedia"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://wireplumber.conf \
    file://00-audio-sink.endpoint \
    file://00-audio-source.endpoint \
    file://00-default-input-audio.endpoint-link \
    file://00-default-output-audio.endpoint-link \
    file://00-stream-input-audio.endpoint \
    file://00-stream-output-audio.endpoint \
    file://01-hw00-audio-sink.endpoint \
    file://01-hw00-audio-source.endpoint \
    file://30-ak4613-audio-sink.endpoint \
    file://30-ak4613-audio-source.endpoint \
    file://30-rcarsound-audio-sink.endpoint \
    file://30-rcarsound-audio-source.endpoint \
    file://30-dra7xx-audio-sink.endpoint \
    file://30-dra7xx-audio-source.endpoint \
    file://30-rpi3-audio-sink.endpoint \
    file://30-imx8mq-audio-sink.endpoint \
    file://40-fiberdyne-amp.endpoint \
    file://40-microchip-mic.endpoint \
    file://70-usb-audio-sink.endpoint \
    file://70-usb-audio-source.endpoint \
    file://bluealsa-input-audio.endpoint-link \
    file://bluealsa-output-audio.endpoint-link \
    file://capture.streams \
    file://playback.streams \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install_append() {
    install -d ${D}/${sysconfdir}/wireplumber/
    install -m 644 ${WORKDIR}/wireplumber.conf ${D}/${sysconfdir}/wireplumber/wireplumber.conf
    install -m 644 ${WORKDIR}/*.endpoint ${D}/${sysconfdir}/wireplumber/
    install -m 644 ${WORKDIR}/*.endpoint-link ${D}/${sysconfdir}/wireplumber/
    install -m 644 ${WORKDIR}/*.streams ${D}/${sysconfdir}/wireplumber/
}

FILES_${PN} += "\
    ${sysconfdir}/wireplumber/* \
"
CONFFILES_${PN} += "\
    ${sysconfdir}/wireplumber/* \
"

RPROVIDES_${PN} += "virtual/wireplumber-config"

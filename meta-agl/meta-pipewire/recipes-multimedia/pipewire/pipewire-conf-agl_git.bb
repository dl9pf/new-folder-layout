SUMMARY     = "AGL configuration file for pipewire"
HOMEPAGE    = "https://pipewire.org"
BUGTRACKER  = "https://jira.automotivelinux.org"
AUTHOR      = "George Kiagiadakis <george.kiagiadakis@collabora.com>"
SECTION     = "multimedia"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
 file://pipewire.conf.in \
 file://client.env \
 file://server.env \
 "

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install_append() {
    # enable optional features in the config
    BLUEZ5=${@bb.utils.contains('DISTRO_FEATURES', 'bluez5', '', '#', d)}
    sed -e "s/#IF_BLUEZ5 /${BLUEZ5}/" ${WORKDIR}/pipewire.conf.in > ${WORKDIR}/pipewire.conf

    # install our custom config
    install -d ${D}/${sysconfdir}/pipewire/
    install -m 0644 ${WORKDIR}/pipewire.conf ${D}${sysconfdir}/pipewire/pipewire.conf

    # install environment variable files
    install -d ${D}/${sysconfdir}/afm/unit.env.d/
    install -m 0644 ${WORKDIR}/client.env ${D}/${sysconfdir}/afm/unit.env.d/pipewire
    install -m 0644 ${WORKDIR}/server.env ${D}${sysconfdir}/pipewire/environment
}

FILES_${PN} = "\
    ${sysconfdir}/pipewire/* \
    ${sysconfdir}/afm/unit.env.d/* \
"
CONFFILES_${PN} += "\
    ${sysconfdir}/pipewire/* \
    ${sysconfdir}/afm/unit.env.d/* \
"

RPROVIDES_${PN} += "virtual/pipewire-config"

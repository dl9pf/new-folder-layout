SRC_URI += "\
    file://pipewire@.service \
    file://pipewire@.socket \
    file://smack-pipewire \
    "

do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        # remove the original user unit files shipped by pipewire
        rm -rf ${D}${systemd_unitdir}

        # install our own system-level templates
        mkdir -p ${D}${systemd_system_unitdir}/
        install -m 0644 ${WORKDIR}/pipewire@.service ${D}${systemd_system_unitdir}/pipewire@.service
        install -m 0644 ${WORKDIR}/pipewire@.socket ${D}${systemd_system_unitdir}/pipewire@.socket

        # enable the socket to start together with afm-user-session
        mkdir -p ${D}${systemd_system_unitdir}/afm-user-session@.target.wants
        ln -sf ../pipewire@.socket ${D}${systemd_system_unitdir}/afm-user-session@.target.wants/pipewire@.socket

        # install smack rules
        mkdir -p ${D}${sysconfdir}/smack/accesses.d
        install -m 0644 ${WORKDIR}/smack-pipewire ${D}${sysconfdir}/smack/accesses.d/pipewire
    fi
}

FILES_${PN} += "\
    ${systemd_system_unitdir}/* \
    ${sysconfdir}/smack/accesses.d/* \
"

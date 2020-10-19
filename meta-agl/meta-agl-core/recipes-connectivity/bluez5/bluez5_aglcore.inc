FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI_append = " \
    file://bluetooth.conf \
    file://tmpdir.conf \
    file://obex.service \
    file://bluetooth.service \
"

FILES_${PN} += "${systemd_user_unitdir}/obex.service.d/tmpdir.conf"

do_install_append() {
    install -m 0644 ${WORKDIR}/bluetooth.conf ${D}${sysconfdir}/dbus-1/system.d/bluetooth.conf

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        mkdir -p ${D}${systemd_user_unitdir}/obex.service.d

        install -m 0644 ${WORKDIR}/obex.service ${D}${systemd_user_unitdir}
        install -m 0644 ${WORKDIR}/tmpdir.conf ${D}${systemd_user_unitdir}/obex.service.d/tmpdir.conf
        mkdir -p ${D}/etc/systemd/user
        ln -sf ${systemd_user_unitdir}/obex.service ${D}/etc/systemd/user/dbus-org.bluez.obex.service
        mkdir -p ${D}${systemd_system_unitdir}
        install -m 0644 ${WORKDIR}/bluetooth.service ${D}${systemd_system_unitdir}
    fi
}

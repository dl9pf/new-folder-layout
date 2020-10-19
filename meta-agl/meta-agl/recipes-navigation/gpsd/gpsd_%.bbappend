SYSTEMD_SERVICE_${PN} += " gpsdctl@.service"

RPROVIDES_${PN}-conf += "virtual/gpsd-conf"

do_install_append() {
    # use the systemd compatible gpsd.rules
    install -d ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 ${S}/gpsd.rules ${D}/${sysconfdir}/udev/rules.d/60-gpsd.rules

    #support for systemd
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/systemd/gpsdctl@.service ${D}${systemd_unitdir}/system

    #autoprobe usb gps devices
    echo 'USBAUTO="true"' >> ${D}${sysconfdir}/default/gpsd.default
}

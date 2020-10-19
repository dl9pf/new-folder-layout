SUMMARY = "USB CAN adapter udev configuration"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://60-usb-can.rules \
           file://slcand@.service \
           file://slcand-default \
"

do_compile[noexec] = "1"

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${sysconfdir}/udev/rules.d
        install -m 0644 ${WORKDIR}/60-usb-can.rules ${D}${sysconfdir}/udev/rules.d/
        install -d ${D}${systemd_system_unitdir}
        install -m 0644 ${WORKDIR}/slcand@.service ${D}${systemd_system_unitdir}/
        install -d ${D}${sysconfdir}/default
        install -m 0644 ${WORKDIR}/slcand-default ${D}${sysconfdir}/default/slcand
    fi
}

FILES_${PN} += "${systemd_system_unitdir}"

RDEPENDS_${PN} += "systemd"

DESCRIPTION = "setup of a system using smack"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "\
  file://55-udev-smack-default.rules \
  file://systemd-journald.service.conf \
  file://systemd-tmpfiles-setup.service.conf \
  file://tmp.mount.conf \
"

RDEPENDS_${PN}_append_with-lsm-smack = " smack"

do_install_append_with-lsm-smack() {
    # tuning systemd units
    install -Dm0644 ${WORKDIR}/systemd-tmpfiles-setup.service.conf \
                     ${D}${systemd_unitdir}/system/systemd-tmpfiles-setup.service.d/smack.conf
    install -Dm0644 ${WORKDIR}/systemd-journald.service.conf \
                     ${D}${systemd_unitdir}/system/systemd-journald.service.d/smack.conf
    install -Dm0644 ${WORKDIR}/tmp.mount.conf \
                     ${D}${systemd_unitdir}/system/tmp.mount.d/smack.conf

    # add udev rules
    install -Dm0644 ${WORKDIR}/55-udev-smack-default.rules \
                     ${D}${sysconfdir}/udev/rules.d/55-udev-smack-default.rules
}

FILES_${PN} += "${systemd_unitdir}"

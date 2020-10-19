DESCRIPTION = "Configure MOST driver"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

RDEPENDS_${PN} = "bash"

SRC_URI = "\
    file://unicens-config.service \
    file://unicens-config.sh \
    file://55-inic.rules \
"


inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "unicens-config.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/unicens-config.service ${D}${systemd_system_unitdir}
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/unicens-config.sh ${D}${bindir}
    install -D -m 0644 ${WORKDIR}/55-inic.rules ${D}${sysconfdir}/udev/rules.d/55-inic.rules
}

FILES_${PN} += "${systemd_system_unitdir}"

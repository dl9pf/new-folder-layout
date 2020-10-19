FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"
FILES_${PN} += "/etc/ti-sgx/"

SRC_URI_append = "\
    file://pvr.service \
"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "pvr.service"

do_install_append() {
	install -d ${D}${systemd_system_unitdir}
	install -m 0755 ${WORKDIR}/pvr.service ${D}${systemd_system_unitdir}
	install -d ${D}/etc/ti-sgx
	install -m 0755 ${D}/etc/init.d/rc.pvr ${D}/etc/ti-sgx
}

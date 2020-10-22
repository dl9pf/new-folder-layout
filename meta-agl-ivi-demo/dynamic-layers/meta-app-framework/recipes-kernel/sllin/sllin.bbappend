FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " \
	file://sllin-demo.service \
"

SYSTEMD_SERVICE_${PN} = "sllin-demo.service"

do_install_append () {
	install -d ${D}${systemd_system_unitdir}
	install -m 0644 ${WORKDIR}/sllin-demo.service ${D}${systemd_system_unitdir}/
}

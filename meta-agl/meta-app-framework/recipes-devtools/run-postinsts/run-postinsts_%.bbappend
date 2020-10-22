FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://ldconfig-wait.conf"

do_configure_append() {
	if ! grep -q StandardOutput= ${WORKDIR}/run-postinsts.service; then
		sed -i '/ExecStart=/iStandardOutput=journal+console' ${WORKDIR}/run-postinsts.service
	fi
}

do_install_append() {
	install -d ${D}${sysconfdir}/systemd/system/run-postinsts.service.d
	install -m 0644 ${WORKDIR}/ldconfig-wait.conf ${D}${sysconfdir}/systemd/system/run-postinsts.service.d
}

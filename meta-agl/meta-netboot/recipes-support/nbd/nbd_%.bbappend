
do_install_append() {
	mv ${D}/${sbindir}/${BPN}-client ${D}/${sbindir}/${BPN}3-client
}

FILES_${PN}-client = "${sbindir}/${BPN}3-client"

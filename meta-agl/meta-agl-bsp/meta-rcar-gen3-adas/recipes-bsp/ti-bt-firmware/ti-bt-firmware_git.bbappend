do_install() {
	install -d  ${D}${nonarch_base_libdir}/firmware/ti-connectivity/
	cp *.bts ${D}${nonarch_base_libdir}/firmware/ti-connectivity/
}

FILES_${PN} = "${nonarch_base_libdir}/firmware/ti-connectivity/*"

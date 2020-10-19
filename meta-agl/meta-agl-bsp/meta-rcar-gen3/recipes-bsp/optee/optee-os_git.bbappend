do_install() {
    install -d ${D}/usr/share/optee
    cp -r ${S}/out/arm-plat-${PLATFORM}/export-ta_arm64 ${D}/usr/share/optee
}

FILES_${PN}-staticdev += "${datadir}/optee/export-ta_arm64/lib/*.a"
FILES_${PN}-dev += "${datadir}/optee/export-ta_arm64"

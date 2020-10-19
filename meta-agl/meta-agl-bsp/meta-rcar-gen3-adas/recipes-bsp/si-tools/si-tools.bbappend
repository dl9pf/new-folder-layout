FILESEXTRAPATHS_append := ":${THISDIR}/files"

SRC_URI += " \
    file://si-tools-fm-improvements.patch \
"

EXTRA_OEMAKE_append = " 'LDFLAGS=${LDFLAGS}'"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${nonarch_base_libdir}/firmware/radio/

    install -m 755 si_ctl ${D}${bindir}
    install -m 755 si_flash ${D}${bindir}
    for file in ${SCRIPTS}; do
        install -m 755 ${S}/scripts/$file ${D}${bindir}
        sed -e 's,^\(SI_ARGS\s*=\s*\).*,\1"/dev/i2c-12 0x65",' -i ${D}${bindir}/$file
    done

    for file in ${FIRMWARE}; do
        install -m 644 ${S}/firmware/$file ${D}${nonarch_base_libdir}/firmware/radio/
    done
}

FILES_${PN} = " \
    ${bindir} \
    ${nonarch_base_libdir}/firmware/radio \
"

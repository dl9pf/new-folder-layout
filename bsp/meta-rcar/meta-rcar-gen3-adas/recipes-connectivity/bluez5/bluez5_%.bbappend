FILESEXTRAPATHS_append := "${THISDIR}/files:"

SRC_URI_append_rcar-gen3 = " \
    file://main.conf \
"

PACKAGECONFIG_append = " experimental"
PACKAGECONFIG[experimental] = "--enable-experimental,--disable-experimental,"

NOINST_TOOLS_EXPERIMENTAL_remove = " tools/bdaddr"


do_install_append_rcar-gen3() {
    install -d ${D}/etc/bluetooth

    install -m 644 ${WORKDIR}/main.conf ${D}/${sysconfdir}/bluetooth/
}

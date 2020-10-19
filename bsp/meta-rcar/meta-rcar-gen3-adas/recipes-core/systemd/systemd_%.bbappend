FILESEXTRAPATHS_append := '${THISDIR}/${PN}:'

SRC_URI_append = " \
    file://70-eth0.network \
    ${@bb.utils.contains("DISTRO_FEATURES", "surroundview", "file://70-dummy0.network", "", d)} \
"

FILES_${PN} += "${sysconfdir}/systemd/network/*"

USERADD_PARAM_${PN} += "; --system systemd-network "

do_install_append() {
    install -d ${D}${sysconfdir}/systemd/network/
    install -m 0644 ${WORKDIR}/*.network  ${D}${sysconfdir}/systemd/network/
}

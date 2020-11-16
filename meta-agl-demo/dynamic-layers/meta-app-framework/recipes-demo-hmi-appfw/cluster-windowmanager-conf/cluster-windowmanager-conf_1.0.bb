SUMMARY = "Cluster demo windowmanager configuration"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://layers.json \
           file://areas.json \
           file://roles.db \
"

do_compile[noexec] = "1"

do_install() {
    install -d ${D}${sysconfdir}/xdg/windowmanager
    install -m 0644 ${WORKDIR}/layers.json ${D}${sysconfdir}/xdg/windowmanager/layers.json
    install -m 0644 ${WORKDIR}/areas.json ${D}${sysconfdir}/xdg/windowmanager/areas.json
    install -m 0644 ${WORKDIR}/roles.db ${D}${sysconfdir}/xdg/windowmanager/roles.db
}

#FILES_${PN} += "${sysconfdir}/*"

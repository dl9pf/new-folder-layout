SUMMARY = "AGL cluster demo dashboard configuration file"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI += "file://AGL.conf.cluster"

inherit allarch

do_install() {
    install -D -m 0644 ${WORKDIR}/AGL.conf.cluster ${D}${sysconfdir}/xdg/AGL.conf
}

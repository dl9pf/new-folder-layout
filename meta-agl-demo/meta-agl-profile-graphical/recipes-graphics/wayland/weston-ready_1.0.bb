SUMMARY = "Weston readiness checker" 
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420" 

inherit systemd
 
SRC_URI = "file://weston-ready \
           file://weston-ready.service \
"
 
do_install() { 
    install -D -m 0755 ${WORKDIR}/weston-ready ${D}${bindir}/weston-ready

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -D -m 0644 ${WORKDIR}/weston-ready.service ${D}${systemd_system_unitdir}/weston-ready.service
    fi
} 

SYSTEMD_SERVICE_${PN} = "weston-ready.service"

RDEPENDS_${PN} += "weston bash"

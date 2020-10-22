SUMMARY = "AGL Login manager"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

LOGIN_USER ??="1001 1002"

RDEPENDS_${PN} += "af-main"

do_install_append() {

    install -d ${D}${systemd_system_unitdir}/multi-user.target.wants/

    for AGL_USER in ${LOGIN_USER};do
        ln -s ../afm-user-session@.service ${D}${systemd_system_unitdir}/multi-user.target.wants/afm-user-session@${AGL_USER}.service
    done
}

FILES_${PN} += "${systemd_system_unitdir}"

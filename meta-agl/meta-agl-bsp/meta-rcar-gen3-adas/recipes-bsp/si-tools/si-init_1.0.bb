SUMMARY     = "Systemd service unit for Si468x radio initialization"
LICENSE     = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit systemd

SRC_URI = "file://si-init.service"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/si-init.service ${D}${systemd_system_unitdir}

    # Add symlink to sysinit.target.wants
    install -d ${D}${sysconfdir}/systemd/system/sysinit.target.wants
    ln -s ${systemd_system_unitdir}/si-init.service ${D}${sysconfdir}/systemd/system/sysinit.target.wants/

    # Add a rule to ensure the 'audio' user has permission to access
    # the Si468x device via i2c
    install -d ${D}${sysconfdir}/udev/rules.d
    cat >${D}${sysconfdir}/udev/rules.d/zz-radio-si.rules <<'EOF'
KERNEL=="i2c-12", MODE="0660", GROUP="audio", SECLABEL{smack}="*"
EOF
}

FILES_${PN} += "${systemd_system_unitdir}"

SUMMARY = "Configuration files for running wayland with a non-weston compositor"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch agl-graphical

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    # Add a rule to ensure the 'display' user has permissions to
    # open the graphics device
    install -d ${D}${sysconfdir}/udev/rules.d
    cat >${D}${sysconfdir}/udev/rules.d/zz-dri.rules <<'EOF'
SUBSYSTEM=="drm", MODE="0660", GROUP="${WESTONGROUP}", SECLABEL{smack}="*"
EOF

    # user 'display' must also be able to access /dev/input/*
    cat >${D}${sysconfdir}/udev/rules.d/zz-input.rules <<'EOF'
SUBSYSTEM=="input", MODE="0660", GROUP="input", SECLABEL{smack}="^"
EOF

    # user 'display' must also be able to access /dev/media*, etc.
    cat >${D}${sysconfdir}/udev/rules.d/zz-remote-display.rules <<'EOF'
SUBSYSTEM=="media", MODE="0660", GROUP="display", SECLABEL{smack}="*"
SUBSYSTEM=="video4linux", MODE="0660", GROUP="display", SECLABEL{smack}="*"
KERNEL=="uvcs", SUBSYSTEM=="misc", MODE="0660", GROUP="display", SECLABEL{smack}="*"
KERNEL=="rgnmm", SUBSYSTEM=="misc", MODE="0660", GROUP="display", SECLABEL{smack}="*"
EOF
}

do_install_append_imx() {
    install -d ${D}${sysconfdir}/udev/rules.d
    cat >>${D}${sysconfdir}/udev/rules.d/zz-dri.rules <<'EOF'
SUBSYSTEM=="gpu_class", MODE="0660", GROUP="${WESTONGROUP}", SECLABEL{smack}="*"
EOF

}

RCONFLICTS_${PN} = "weston-init"

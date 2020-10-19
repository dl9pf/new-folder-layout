COMPATIBLE_MACHINE = "raspberrypi4"
TFA_BUILD_TARGET = "bl31"
TFA_PLATFORM = "rpi4"

# Skip installing the binary into /lib/firmware. We only need it on the boot
# partition that is generated from the files in DEPLOYDIR
do_install[noexec] = "1"

FILES_${PN} = ""

do_deploy() {
    if ${@"true" if d.getVar('TFA_DEBUG') == '1' else "false"}; then
        BUILD_PLAT=${B}/${BUILD_DIR}/debug/
    else
        BUILD_PLAT=${B}/${BUILD_DIR}/release/
    fi

    install -d ${DEPLOYDIR}/bcm2835-bootfiles
    cp ${BUILD_PLAT}/bl31.bin ${DEPLOYDIR}/bcm2835-bootfiles/bl31.bin
}


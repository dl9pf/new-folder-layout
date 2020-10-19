DISABLE_OVERSCAN = "1"
TOTAL_BOARD_MEM = "3072"

do_deploy_append_raspberrypi4() {
    # ENABLE CAN
    if [ "${ENABLE_CAN}" = "1" ]; then
        echo "# Enable CAN" >>${DEPLOYDIR}/bootfiles/config.txt
        echo "dtoverlay=mcp2515-can0,oscillator=16000000,interrupt=25" >>${DEPLOYDIR}/bootfiles/config.txt
    fi

    # Handle setup with armstub file
    if [ -n "${ARMSTUB}" ]; then
        echo "\n# ARM stub configuration" >> ${DEPLOYDIR}/bootfiles/config.txt
        echo "armstub=${ARMSTUB}" >> ${DEPLOYDIR}/bootfiles/config.txt
        case "${ARMSTUB}" in
            *-gic.bin)
                echo  "enable_gic=1" >> ${DEPLOYDIR}/bootfiles/config.txt
                ;;
        esac
    fi

    if [ "${AGL_XEN_WANTED}" = "1" ]; then
        echo "total_mem=${TOTAL_BOARD_MEM}" >> ${DEPLOYDIR}/bootfiles/config.txt
    fi
}

do_deploy_append() {
    if [ "${ENABLE_CMA}" = "1" ] && [ -n "${CMA_LWM}" ]; then
        sed -i '/#cma_lwm/ c\cma_lwm=${CMA_LWM}' ${DEPLOYDIR}/bootfiles/config.txt
    fi

    if [ "${ENABLE_CMA}" = "1" ] && [ -n "${CMA_HWM}" ]; then
        sed -i '/#cma_hwm/ c\cma_hwm=${CMA_HWM}' ${DEPLOYDIR}/bootfiles/config.txt
    fi

    echo "avoid_warnings=2" >> ${DEPLOYDIR}/bootfiles/config.txt
    echo "mask_gpu_interrupt0=0x400" >> ${DEPLOYDIR}/bootfiles/config.txt
    echo "dtoverlay=vc4-kms-v3d-overlay,cma-256" >> ${DEPLOYDIR}/bootfiles/config.txt
    echo "dtoverlay=rpi-ft5406-overlay" >> ${DEPLOYDIR}/bootfiles/config.txt
    echo "dtparam=audio=on" >> ${DEPLOYDIR}/bootfiles/config.txt
}

do_deploy_append_raspberrypi4() {
    echo -e "\n[pi4]" >> ${DEPLOYDIR}/bootfiles/config.txt
    echo "max_framebuffers=2" >> ${DEPLOYDIR}/bootfiles/config.txt
}

do_deploy_append_sota() {
    echo "device_tree_address=0x0c800000" >> ${DEPLOYDIR}/bootfiles/config.txt
}

ENABLE_UART_raspberrypi3 = "1"
ENABLE_UART_raspberrypi4 = "1"

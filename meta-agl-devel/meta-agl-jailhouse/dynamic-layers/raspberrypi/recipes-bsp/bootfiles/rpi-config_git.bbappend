
do_deploy_append_raspberrypi4() {
    # if ARMSTUB is set, it should be set in config.txt by earlier recipes, so replace it
    if [ -n "${ARMSTUB}" ]; then
        sed -i 's/^armstub=.*/armstub=bl31.bin/' ${DEPLOYDIR}/bcm2835-bootfiles/config.txt

	if ! grep '^enable_gic' config.txt; then
	        sed -i 's/^enable_gic=.*/enable_gic=1/' ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
	else
		echo "enable_gic=1" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
	fi	
	
    # otherwise, set it
    else
        echo "# ARM stub configuration" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
        echo "armstub=bl31.bin" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
        echo "enable_gic=1" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
    fi

    sed -i -e "s#dtoverlay=mcp2515.*##g" ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
    echo "# Enable CAN (Waveshare RS485 CAN HAT)" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
    echo "dtoverlay=mcp2515-can0,oscillator=8000000,interrupt=25,spimaxfrequency=1000000" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt

    # memory reserved for Jailhouse
    echo "dtoverlay=jailhouse-memory" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt
    echo "dtoverlay=jailhouse-memory,start=0xe0000000,size=0x200000" >> ${DEPLOYDIR}/bcm2835-bootfiles/config.txt


}



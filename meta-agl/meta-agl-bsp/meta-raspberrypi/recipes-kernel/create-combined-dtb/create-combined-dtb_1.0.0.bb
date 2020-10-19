SUMMARY = "Combine dtb and dtbo"
DESCRIPTION = "Combine specified dtb and one or more dtbo into specified filename found in deploydir"
SECTION = "bootloader"
PR = "r1"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

DEPENDS = "dtc-native"

ALLOW_EMPTY_${PN} = "1"
FILES_${PN} = ""

S = "${WORKDIR}"

do_compile[depends] += "virtual/kernel:do_deploy"

do_compile () {
	# Official touchscreen setup (rpi3b/rpi3b dtb, VC4DTBO and ft5406)
	if [ -f "${DEPLOY_DIR_IMAGE}/bcm2710-rpi-3-b-plus.dtb" ]; then
		fdtoverlay -v -i ${DEPLOY_DIR_IMAGE}/bcm2710-rpi-3-b-plus.dtb -o bcm2710-rpi-3-b-plus+vc4+ft5406.dtb ${DEPLOY_DIR_IMAGE}/rpi-ft5406.dtbo ${DEPLOY_DIR_IMAGE}/${VC4DTBO}.dtbo
	fi
	if [ -f "${DEPLOY_DIR_IMAGE}/bcm2710-rpi-3-b.dtb" ]; then
		fdtoverlay -v -i ${DEPLOY_DIR_IMAGE}/bcm2710-rpi-3-b.dtb -o bcm2710-rpi-3-b+vc4+ft5406.dtb ${DEPLOY_DIR_IMAGE}/rpi-ft5406.dtbo ${DEPLOY_DIR_IMAGE}/${VC4DTBO}.dtbo
	fi
	# NOTE: meta-updater currently disables rpi-ft5406.dtbo on rpi4, so need to check if it is present
	if [ -f "${DEPLOY_DIR_IMAGE}/bcm2711-rpi-4-b.dtb" -a -f "${DEPLOY_DIR_IMAGE}/rpi-ft5406.dtbo" ]; then
		fdtoverlay -v -i ${DEPLOY_DIR_IMAGE}/bcm2711-rpi-4-b.dtb -o bcm2711-rpi-4-b+vc4+ft5406.dtb ${DEPLOY_DIR_IMAGE}/rpi-ft5406.dtbo ${DEPLOY_DIR_IMAGE}/${VC4DTBO}.dtbo
	fi

	# HDMI screen setup (rpi3b/rpi3b dtb and VC4DTBO)
	if [ -f "${DEPLOY_DIR_IMAGE}/bcm2710-rpi-3-b-plus.dtb" ]; then
		fdtoverlay -v -i ${DEPLOY_DIR_IMAGE}/bcm2710-rpi-3-b-plus.dtb -o bcm2710-rpi-3-b-plus+vc4.dtb ${DEPLOY_DIR_IMAGE}/${VC4DTBO}.dtbo
	fi
	if [ -f "${DEPLOY_DIR_IMAGE}/bcm2710-rpi-3-b.dtb" ]; then
		fdtoverlay -v -i ${DEPLOY_DIR_IMAGE}/bcm2710-rpi-3-b.dtb -o bcm2710-rpi-3-b+vc4.dtb ${DEPLOY_DIR_IMAGE}/${VC4DTBO}.dtbo
	fi
	if [ -f "${DEPLOY_DIR_IMAGE}/bcm2711-rpi-4-b.dtb" ]; then
		fdtoverlay -v -i ${DEPLOY_DIR_IMAGE}/bcm2711-rpi-4-b.dtb -o bcm2711-rpi-4-b+vc4.dtb ${DEPLOY_DIR_IMAGE}/${VC4DTBO}.dtbo
	fi
}

do_deploy () {
	install -d ${DEPLOY_DIR_IMAGE}
	if [ -f "${S}/bcm2711-rpi-4-b+vc4+ft5406.dtb" ]; then
		install -m 0644 ${S}/bcm2711-rpi-4-b+vc4+ft5406.dtb ${DEPLOY_DIR_IMAGE}
	fi
	if [ -f "${S}/bcm2710-rpi-3-b+vc4+ft5406.dtb" ]; then
		install -m 0644 ${S}/bcm2710-rpi-3-b+vc4+ft5406.dtb ${DEPLOY_DIR_IMAGE}
	fi
	if [ -f "${S}/bcm2710-rpi-3+vc4+ft5406.dtb" ]; then
		install -m 0644 ${S}/bcm2710-rpi-3+vc4+ft5406.dtb ${DEPLOY_DIR_IMAGE}
	fi
	if [ -f "${S}/bcm2711-rpi-4-b+vc4.dtb" ]; then
		install -m 0644 ${S}/bcm2711-rpi-4-b+vc4.dtb ${DEPLOY_DIR_IMAGE}
	fi
	if [ -f "${S}/bcm2710-rpi-3-b+vc4.dtb" ]; then
		install -m 0644 ${S}/bcm2710-rpi-3-b+vc4.dtb ${DEPLOY_DIR_IMAGE}
	fi
	if [ -f "${S}/bcm2710-rpi-3+vc4.dtb" ]; then
		install -m 0644 ${S}/bcm2710-rpi-3+vc4.dtb ${DEPLOY_DIR_IMAGE}
	fi
}

addtask deploy after do_install

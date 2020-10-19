DESCRIPTION = "Xen hypervisor u-boot image"
LICENSE = "GPLv2"
SECTION = "console/tools"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"



ALLOW_EMPTY_${PN} = "1"
FILES_${PN} = ""
S = "${WORKDIR}"

DEPENDS = "u-boot-mkimage-native "

# Only for aarch64
COMPATIBLE_MACHINE = "(-)"
COMPATIBLE_MACHINE_aarch64 = "(.*)"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_configure[noexec] = "1"
do_deploy[noexec] = "1"

do_compile[depends] += "xen:do_deploy"

# Uboot compatible image
do_compile () {
        uboot-mkimage -A arm64 -C none -T kernel -a 0x48080000 -e 0x48080000 -n "XEN" -d ${DEPLOY_DIR_IMAGE}/xen-${MACHINE} ${DEPLOY_DIR_IMAGE}/xen-${MACHINE}.uImage
}

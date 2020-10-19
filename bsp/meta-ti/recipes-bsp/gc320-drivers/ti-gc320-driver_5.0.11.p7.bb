DESCRIPTION = "Kernel drivers for the Vivante GC320 chipset found in TI SoCs"
HOMEPAGE = "https://git.ti.com/graphics/ti-gc320-driver"
LICENSE = "MIT | GPLv2"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=78d9818a51b9a8e9bb89dea418bac297"

inherit module features_check

REQUIRED_MACHINE_FEATURES = "gc320"

MACHINE_KERNEL_PR_append = "h"
PR = "${MACHINE_KERNEL_PR}"

# Need to branch out with ${PV} var
BRANCH = "ti-${PV}-k5.4"

SRCREV = "2a4fbe8353fa56011a613aeeaf69e274cd07a825"

SRC_URI = "git://git.ti.com/graphics/ti-gc320-driver.git;protocol=git;branch=${BRANCH}"

S = "${WORKDIR}/git/src"

EXTRA_OEMAKE += "-f Kbuild AQROOT=${S} KERNEL_DIR=${STAGING_KERNEL_DIR} TOOLCHAIN_PATH=${TOOLCHAIN_PATH} CROSS_COMPILE=${TARGET_PREFIX} ARCH_TYPE=${TARGET_ARCH}"

do_install() {
    install -d ${D}/${base_libdir}/modules/${KERNEL_VERSION}/extra
    install -m 644 ${S}/galcore.ko ${D}/${base_libdir}/modules/${KERNEL_VERSION}/extra
}

COMPATIBLE_HOST ?= "null"
COMPATIBLE_HOST_ti-soc = "(.*)"

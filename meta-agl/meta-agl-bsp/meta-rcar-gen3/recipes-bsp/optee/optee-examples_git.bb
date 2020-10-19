SUMMARY = "OP-TEE examples"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=cd95ab417e23b94f381dafc453d70c30"


PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit python3native

PV = "3.6.0+git${SRCPV}"

SRCREV = "292da2fd8db0176e0e96989268c63ef9ab910a6c"

SRC_URI = " \
    git://github.com/linaro-swg/optee_examples;branch=master;name=master \
    file://0001-secure_storage-ta-Add-a-missing-include-file.patch \
"

COMPATIBLE_MACHINE = "(salvator-x|h3ulcb|m3ulcb|m3nulcb|ebisu)"
PLATFORM = "rcar"

DEPENDS = "optee-os optee-client python-pycrypto-native"

CFLAGS += "-Wno-extra -Wno-error=format"
TARGET_CFLAGS += "-Wno-extra -Wno-error=format"

TARGET_CC_ARCH += "${LDFLAGS}"
INSANE_SKIP_${PN} = "ldflags"

TA_DEV_KIT_DIR = "${STAGING_DIR_TARGET}/usr/share/optee/export-ta_arm64"

OPTEE_CLIENT_EXPORT = "${STAGING_DIR_TARGET}/usr"

TEEC_EXPORT = "${STAGING_DIR_TARGET}/usr"

S = "${WORKDIR}/git"
EXTRA_OEMAKE = "\
                TEEC_EXPORT=${TEEC_EXPORT} \
                OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
                TA_DEV_KIT_DIR=${TA_DEV_KIT_DIR} \
                HOST_CROSS_COMPILE=${TARGET_PREFIX} \
                TA_CROSS_COMPILE=${TARGET_PREFIX} \
                PLATFORM=${PLATFORM} \
                V=1 \
                "

do_compile() {
    oe_runmake
}

do_install () {
    mkdir -p ${D}${nonarch_base_libdir}/optee_armtz
    mkdir -p ${D}${bindir}
    install -D -p -m0755 ${S}/out/ca/* ${D}${bindir}
    install -D -p -m0444 ${S}/out/ta/* ${D}${nonarch_base_libdir}/optee_armtz
    mkdir -p ${D}${includedir}
    cp ${S}/hello_world/ta/include/* ${D}${includedir}
}

FILES_${PN} += "${nonarch_base_libdir}/optee_armtz/"

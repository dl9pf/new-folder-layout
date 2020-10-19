DESCRIPTION = "OP-TEE TEST"

LICENSE = "GPLv2 & BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${S}/host/LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit python3native

PV = "3.1.0+git${SRCPV}"

SRCREV = "45218eb59b006ad20cc7610904f291dd85157a43"

SRC_URI = " \
    git://github.com/OP-TEE/optee_test.git;branch=master;name=master \
    file://optee_xtest_fix.diff \
"

COMPATIBLE_MACHINE = "(salvator-x|h3ulcb|m3ulcb|m3nulcb|ebisu)"
PLATFORM = "rcar"

DEPENDS = "optee-os optee-client python3-pycrypto-native"

export CROSS_COMPILE64="${TARGET_PREFIX}"

# Let the Makefile handle setting up the flags as it is a standalone application
LD[unexport] = "1"
LDFLAGS[unexport] = "1"
export CCcore="${CC}"
export LDcore="${LD}"
libdir[unexport] = "1"

CFLAGS += "-Wno-extra -Wno-error=stringop-overflow -Wno-error=array-bounds"
TARGET_CFLAGS += "-Wno-extra -Wno-error=stringop-overflow -Wno-error=array-bounds"

TARGET_CC_ARCH += "${LDFLAGS}"
INSANE_SKIP_${PN} = "ldflags"

S = "${WORKDIR}/git"
EXTRA_OEMAKE = "-e MAKEFLAGS="

do_compile() {
    oe_runmake CROSS_COMPILE=${CROSS_COMPILE64} PLATFORM=${PLATFORM} OPTEE_CLIENT_EXPORT=${STAGING_DIR_TARGET}/usr --no-builtin-variables TA_DEV_KIT_DIR="${STAGING_DIR_TARGET}/usr/share/optee/export-ta_arm64"
}

do_install () {
    install -D -p -m0755 ${S}/out/xtest/xtest ${D}${bindir}/xtest

    # install path should match the value set in optee-client/tee-supplicant
    # default TEEC_LOAD_PATH is /lib
    mkdir -p ${D}${nonarch_base_libdir}/optee_armtz/
    install -D -p -m0444 ${S}/out/ta/*/*.ta ${D}${nonarch_base_libdir}/optee_armtz/
}

FILES_${PN} += "${nonarch_base_libdir}/optee_armtz/"

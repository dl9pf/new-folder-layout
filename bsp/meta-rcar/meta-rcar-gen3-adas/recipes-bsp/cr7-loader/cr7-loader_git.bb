DESCRIPTION = "CR7 Loader"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.md;md5=9b6b96211116d6143a7f1d681d39b13d"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "cr7-loader-tools-native"

inherit deploy

S = "${WORKDIR}/git"

BRANCH = "rcar_gen3"
SRC_URI = "git://github.com/CogentEmbedded/cr7-loader.git;branch=${BRANCH}"
SRCREV = "f665a20984fbfcf6321f459f40dc0d419e310da2"

PV = "v1.0+renesas+git"

COMPATIBLE_MACHINE = "eagle|condor|v3msk|v3mzf|v3hsk"
PLATFORM = "rcar"

EXTRA_OEMAKE_r8a77970 = "LSI=V3M RCAR_DRAM_SPLIT=0 RCAR_KICK_MAIN_CPU=2 RCAR_SECURE_BOOT=0"
EXTRA_OEMAKE_r8a77980 = "LSI=V3H RCAR_DRAM_SPLIT=0 RCAR_KICK_MAIN_CPU=2 RCAR_SECURE_BOOT=0 RCAR_QOS_TYPE=0"

EXTRA_OEMAKE_prepend = "CROSS_COMPILE=${STAGING_LIBEXECDIR_NATIVE}/cr7-loader-tools-native/bin/arm-eabi- "

do_deploy() {
    # Create deploy folder
    install -d ${DEPLOYDIR}

    # Copy CR7 Loader to deploy folder
    install -m 0644 ${S}/cr7_loader.elf ${DEPLOYDIR}/cr7-${MACHINE}.elf
    install -m 0644 ${S}/cr7_loader.bin ${DEPLOYDIR}/cr7-${MACHINE}.bin
    install -m 0644 ${S}/cr7_loader.srec ${DEPLOYDIR}/cr7-${MACHINE}.srec

    install -m 0644 ${S}/bootparam_sa0.bin ${DEPLOYDIR}/bootparam_sa0.bin
    install -m 0644 ${S}/bootparam_sa0.srec ${DEPLOYDIR}/bootparam_sa0.srec

    install -m 0644 ${S}/cert_header_sa3.bin ${DEPLOYDIR}/cert_header_sa3.bin
    install -m 0644 ${S}/cert_header_sa3.srec ${DEPLOYDIR}/cert_header_sa3.srec
}

addtask deploy before do_build after do_compile

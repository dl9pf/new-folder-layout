SUMMARY = "TI DSP Code Generation Tools"
DESCRIPTION = "Texas Instruments (TI) Code Generation Tools are custom \
utilities targeted for TI embedded processors. This Digital Signal \
Processor (DSP) suite contains tools needed to create and debug \
applications for the C7000 DSP family. This includes tools such as: \
compiler, linker, assembler, etc. This also includes C runtime \
libraries and standard header files needed to produce a working DSP application."
HOMEPAGE = "https://www-a.ti.com/downloads/sds_support/TICodegenerationTools/download.htm"
LICENSE = "(TI-TSPA & Thai-Open-Source-Software-Center) & BSD-3-Clause & BSL-1.0 & Patrick-Powell & AFL-3.0 & MIT & BSD-2-Clause & PD"

LIC_FILES_CHKSUM = "file://ti-cgt-c7000_${PV}.STS/C7000_Code_Generation_Tools_1.x_manifest.html;md5=3ee1c9f774004535003f80cb8142bb0f"

require recipes-ti/includes/ti-unpack.inc
require recipes-ti/includes/ti-paths.inc

# only x86_64 is supported
COMPATIBLE_HOST = "x86_64.*-linux"
COMPATIBLE_HOST_class-target = "null"

BINFILE = "ti_cgt_c7000_${PV}.STS_linux_installer_x86.bin"
BINFILE_NAME = "cgt7x_x86_installer"

SRC_URI = "http://software-dl.ti.com/codegen/esd/cgt_public_sw/C7000/${PV}.STS/${BINFILE};name=${BINFILE_NAME}"

TI_BIN_UNPK_ARGS = "--prefix ${S}"
TI_BIN_UNPK_CMDS = ""

SRC_URI[cgt7x_x86_installer.sha256sum] = "24071fe0369e55af80e334852cda7fa78b64ae79a411c57ac6995470a7a23694"

S = "${WORKDIR}/c7000_${PV}"

do_install() {
    install -d ${D}/${TI_CGT7X_INSTALL_DIR_RECIPE}
    cp -rP --preserve=mode,links,timestamps --no-preserve=ownership ${WORKDIR}/c7000_${PV}/ti-cgt-c7000_${PV}.STS/. ${D}/${TI_CGT7X_INSTALL_DIR_RECIPE}
}

FILES_${PN} += "${TI_CGT7X_INSTALL_DIR_RECIPE}"

INSANE_SKIP_${PN} += "arch staticdev textrel file-rdeps"

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

BBCLASSEXTEND = "native nativesdk"

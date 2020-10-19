SUMMARY = "Tools for building cr7 loader"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PV = "5.1-2015.08"

EABI = "arm-eabi"
TOOLS = "gcc-linaro-${PV}-${BUILD_ARCH}_${EABI}"

inherit native

SRC_URI = " \
    https://releases.linaro.org/components/toolchain/binaries/${PV}/${EABI}/${TOOLS}.tar.xz \
"
SRC_URI[md5sum] = "a58cbee5848f6d2361c76a425c24890f"
SRC_URI[sha256sum] = "440f301e5d776a0d61ccc63954e7297c327d3d52dcdd7641b38a98043bcb7704"

do_unpack[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${libexecdir}
    tar -xvf ${DL_DIR}/${TOOLS}.tar.xz -C ${D}${libexecdir}
    ln -sf ${TOOLS} ${D}${libexecdir}/${PN}
}

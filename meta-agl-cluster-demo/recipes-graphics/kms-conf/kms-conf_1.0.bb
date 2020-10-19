SUMMARY = "kms configuration file for the qt eglfs platform"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://kms.conf \
"

do_install_append() {
    install -d ${D}/etc
    install -m 644 ${WORKDIR}/kms.conf ${D}/etc
}

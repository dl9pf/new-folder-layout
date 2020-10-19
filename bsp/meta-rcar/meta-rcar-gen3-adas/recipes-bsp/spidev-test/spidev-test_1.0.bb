SUMMARY = "SPI device test utility"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d47c37512bd65656e8f130581ee80783"

S = "${WORKDIR}/spidev-test"

SRC_URI = " \
    file://spidev-test.tar.gz \
"

do_compile() {
    cd ${S}
    make all || die
}

do_install() {
    install -d ${D}${bindir}
    install -m 755 ${S}/spidev_test ${D}${bindir}
}

FILES_${PN} = "${bindir}/spidev_test"

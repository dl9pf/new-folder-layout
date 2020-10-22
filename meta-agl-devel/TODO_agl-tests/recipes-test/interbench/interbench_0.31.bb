DESCRIPTION = "The Linux interactivity benchmark"
HOMEPAGE = "http://users.tpg.com.au/ckolivas/interbench/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"
SRC_URI = "http://ck.kolivas.org/apps/interbench/interbench-${PV}.tar.bz2"

SRC_URI += "file://interbench.c.patch;striplevel=0"

inherit autotools

INSANE_SKIP_${PN} = "ldflags"

B = "${S}"
EXTRA_OEMAKE = "CC='${CC}'"

# upstream tarball contains x86_64 binaries, we need a clean source tree
do_compile() {
    oe_runmake clean
    oe_runmake
}

do_install() {
    install -d ${D}${bindir} ${D}${datadir}/doc/${PN}/ ${D}${mandir}/man8/
    install -m 0755 ${B}/interbench ${D}${bindir}
    install -m 0644 ${S}/readme* ${D}${datadir}/doc/${PN}/
    install -m 0644 ${S}/interbench.8 ${D}${mandir}/man8/
}

SRC_URI[md5sum] = "832254d7fd1255c548ebea7b97f01015"
SRC_URI[sha256sum] = "89d438b28aef22d26e79812762a57a9f9344a8dd8826edebfe60dad48ee1c784"


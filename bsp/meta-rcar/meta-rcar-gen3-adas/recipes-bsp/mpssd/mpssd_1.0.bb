SUMMARY = "MPSSD"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=58dd7e3251f8a7d8c3784355098b8d53"

DEPENDS = "virtual/kernel"

export KERNELDIR = "${STAGING_KERNEL_DIR}"

S = "${WORKDIR}/mpssd"

SRC_URI = " \
    file://mpssd.tar.gz \
"

do_compile() {
    cd ${S}
    make all || die
}

do_install() {
    install -d ${D}${bindir}
    install -m 755 ${S}/mpssd ${D}${bindir}
}

FILES_${PN} = " \
    ${bindir}/mpssd \
"

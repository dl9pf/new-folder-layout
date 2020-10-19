SUMMARY = "ipv6connect test"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/third_party/autotest/+/0.12.369.B/client/tests/ipv6connect"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/${PN}/LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/qa-test-misc;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "b355b7e1f43a19938895964f314484ffd8a39542"

inherit autotools

INSANE_SKIP_${PN} = "ldflags"

B = "${WORKDIR}/git/ipv6connect"
S = "${WORKDIR}/git"

# configure script is not delivered, only a makefile.
do_configure() {
    :
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/ipv6connect ${D}${bindir}
}

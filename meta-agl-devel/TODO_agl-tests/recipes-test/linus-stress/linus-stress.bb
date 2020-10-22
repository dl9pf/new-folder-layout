SUMMARY = "linus_stress test"
HOMEPAGE = "https://chromium.googlesource.com/chromiumos/third_party/autotest/+/master/client/tests/linus_stress"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/linus_stress/LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/qa-test-misc;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "87cdfd4626c0cb47fc22f328867e49d6268df85c"

inherit autotools
INSANE_SKIP_${PN} = "ldflags"

B = "${WORKDIR}/git/linus_stress"
S = "${WORKDIR}/git"

# configure script is not delivered, only a makefile.
do_configure() {
    :
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/linus_stress ${D}${bindir}
}

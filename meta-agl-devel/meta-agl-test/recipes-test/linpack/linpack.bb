SUMMARY = "Linpack benchmark"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/${PN}/linpack.c;md5=076f1fd8d313056103f98d4253862eae"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/qa-test-misc;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "920cae73a918d1f516113b0ae967be3ecaba86ef"

DEPENDS += "rsync-native"

inherit autotools

S = "${WORKDIR}/git"

# configure script is not delivered, only a makefile.
do_configure() {
    mkdir -p ${B}
    rsync -a ${S}/${PN}/* ${B}
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/linpack ${D}${bindir}
}

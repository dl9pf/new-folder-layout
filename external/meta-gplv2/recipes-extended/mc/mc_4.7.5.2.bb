SUMMARY = "Midnight Commander is an ncurses based file manager"
HOMEPAGE = "http://www.midnight-commander.org/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"
SECTION = "console/utils"
DEPENDS = "ncurses glib-2.0"
RDEPENDS_${PN} = "ncurses-terminfo"

PR = "r3"

SRC_URI = "http://www.midnight-commander.org/downloads/${BPN}-${PV}.tar.bz2 \
           file://mc-CTRL.patch \
           file://0001-disable-Ctrl-g-key-and-Ctrl-key.patch \
           file://0001-Fix-builds-wit-the-gettext-0.20.patch \
           "

SRC_URI[md5sum] = "bdae966244496cd4f6d282d80c9cf3c6"
SRC_URI[sha256sum] = "a68338862bb30017eb65ed569a58e80ab66ae8cef11c886440c9e9f4d1efc6ab"

inherit autotools gettext pkgconfig

EXTRA_OECONF = "--with-screen=ncurses --without-gpm-mouse --without-x --without-samba"

do_install_append () {
	sed -i -e '1s,#!.*perl,#!${bindir}/env perl,' ${D}${libexecdir}/mc/extfs.d/*
	# These use python2 which is no longer supported
	rm ${D}${libexecdir}/mc/extfs.d/s3+
	rm ${D}${libexecdir}/mc/extfs.d/uc1541
}

PACKAGES =+ "${BPN}-helpers-perl ${BPN}-helpers ${BPN}-fish"

SUMMARY_${BPN}-helpers-perl = "Midnight Commander Perl-based helper scripts"
FILES_${BPN}-helpers-perl = "${libexecdir}/mc/extfs.d/a+ ${libexecdir}/mc/extfs.d/apt+ \
                             ${libexecdir}/mc/extfs.d/deb ${libexecdir}/mc/extfs.d/deba \
                             ${libexecdir}/mc/extfs.d/debd ${libexecdir}/mc/extfs.d/dpkg+ \
                             ${libexecdir}/mc/extfs.d/mailfs ${libexecdir}/mc/extfs.d/patchfs \ 
                             ${libexecdir}/mc/extfs.d/rpms+ ${libexecdir}/mc/extfs.d/ulib \ 
                             ${libexecdir}/mc/extfs.d/uzip"
RDEPENDS_${BPN}-helpers-perl = "perl"

SUMMARY_${BPN}-helpers = "Midnight Commander shell helper scripts"
FILES_${BPN}-helpers = "${libexecdir}/mc/extfs.d/* ${libexecdir}/mc/ext.d/*"

SUMMARY_${BPN}-fish = "Midnight Commander Fish scripts"
FILES_${BPN}-fish = "${libexecdir}/mc/fish"


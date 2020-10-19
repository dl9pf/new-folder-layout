SUMMARY = "Turns a Realtek RTL2832U-based DVB dongle into a SDR receiver"
DESCRIPTION = "DVB-T dongles based on the Realtek RTL2832U chipset can be used as Software Digital Radio adapters, since the chip allows transferring raw I/Q samples to the host, which is really used for DAB/DAB+/FM demodulation."
HOMEPAGE = "http://sdr.osmocom.org/trac/wiki/rtl-sdr"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://src/librtlsdr.c;endline=18;md5=1b05599c3ebd4d74857a0a7c45f3d4ef"

DEPENDS = "libusb1"

SRC_URI = "git://git.osmocom.org/rtl-sdr \
           file://0001-remove-I-usr-include-in-pkg-config.patch \
           "
SRCREV = "e3c03f738f5aef4dc51e2b741fbdb542b9cc1bb1"
S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF = "--enable-driver-detach"

do_configure_append() {
    # remove included temporary path from pc file due to pass-through of OE CFLAGS
    # debug-prefix-map
    # See: http://lists.openembedded.org/pipermail/openembedded-devel/2016-May/107456.html
    sed -i -e "s# -fdebug-prefix-map=.*##g;s# -fmacro-prefix-map=.*##g" librtlsdr.pc
}

do_install_append() {
    install -D -m 0644 ${S}/rtl-sdr.rules ${D}${sysconfdir}/udev/rules.d/55-rtl-sdr.rules
    sed -i 's/MODE:="0666"/GROUP="audio", MODE="0660", SECLABEL{smack}="*"/' \
        ${D}${sysconfdir}/udev/rules.d/55-rtl-sdr.rules
}

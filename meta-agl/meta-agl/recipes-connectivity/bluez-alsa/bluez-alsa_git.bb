SUMMARY = "Bluetooth Audio ALSA Backend"
HOMEPAGE = "https://github.com/Arkq/bluez-alsa"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3d7d6ac7e2dbd2505652dceb3acdf1fe"

SRC_URI = "git://github.com/Arkq/bluez-alsa.git;protocol=https;branch=master"
SRCREV = "2cd6e4686f7808276480b430fb37df55dfdcc02b"

SRC_URI += "file://bluez-alsa.service"

S  = "${WORKDIR}/git"

DEPENDS += "alsa-lib bluez5 systemd glib-2.0 sbc"

PACKAGECONFIG[aac]  = "--enable-aac, --disable-aac, "
PACKAGECONFIG[aptx] = "--enable-aptx,--disable-aptx,"
PACKAGECONFIG[ofono] = "--enable-ofono, --disable-ofono,"

inherit autotools pkgconfig
inherit systemd

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "bluez-alsa.service"

PACKAGECONFIG += "ofono"

# enable debug tools in devel images
PACKAGECONFIG[hcitop] = "--enable-hcitop, --disable-hcitop, libbsd ncurses"
PACKAGECONFIG[rfcomm] = "--enable-rfcomm, --disable-rfcomm,"
PACKAGECONFIG_append_agl-devel = " hcitop rfcomm"

do_install_append () {
    install -d ${D}${base_libdir}/systemd/system
    install -m 0644 ${WORKDIR}/bluez-alsa.service ${D}${base_libdir}/systemd/system
}

FILES_${PN} += "\
   ${datadir}/alsa/alsa.conf.d/20-bluealsa.conf\
   ${libdir}/alsa-lib/libasound_module_ctl_bluealsa.so\
   ${libdir}/alsa-lib/libasound_module_pcm_bluealsa.so\
"

SUMMARY     = "Multimedia processing server for Linux"
HOMEPAGE    = "https://pipewire.org"
BUGTRACKER  = "https://github.com/PipeWire/pipewire/issues"
AUTHOR      = "Wim Taymans <wtaymans@redhat.com>"
SECTION     = "multimedia"

LICENSE = "MIT & LGPL-2.1"
LIC_FILES_CHKSUM = "\
    file://COPYING;beginline=3;md5=b3adc775ca6ee80056383a5ae814cc75 \
    file://pipewire-alsa/LICENSE;md5=fc178bcd425090939a8b634d1d6a9594 \
    file://pipewire-jack/LICENSE;md5=fc178bcd425090939a8b634d1d6a9594 \
    file://pipewire-pulseaudio/LICENSE;md5=fc178bcd425090939a8b634d1d6a9594 \
"

inherit meson pkgconfig systemd manpages

DEPENDS = "dbus"

PACKAGECONFIG ??= "\
    ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'bluez5', d)} \
    alsa audioconvert audiomixer \
    pipewire-alsa \
    gstreamer \
"

GST_VER = "1.0"

# systemd integration
PACKAGECONFIG[systemd] = "-Dsystemd=true,-Dsystemd=false,systemd"

# SPA plugins
PACKAGECONFIG[alsa] = "-Dalsa=true,-Dalsa=false,udev alsa-lib"
PACKAGECONFIG[audioconvert] = "-Daudioconvert=true,-Daudioconvert=false,"
PACKAGECONFIG[audiomixer] = "-Daudiomixer=true,-Daudiomixer=false,"
PACKAGECONFIG[audiotestsrc] = "-Daudiotestsrc=true,-Daudiotestsrc=false, "
PACKAGECONFIG[bluez5] = "-Dbluez5=true,-Dbluez5=false,bluez5 sbc"
PACKAGECONFIG[jack] = "-Djack=true,-Djack=false,jack"
PACKAGECONFIG[v4l2] = "-Dv4l2=true,-Dv4l2=false,udev v4l-utils"
PACKAGECONFIG[videotestsrc] = "-Dvideotestsrc=true,-Dvideotestsrc=false, "
PACKAGECONFIG[vulkan] = "-Dvulkan=true,-Dvulkan=false,vulkan"

# alsa plugin to redirect audio to pipewire
PACKAGECONFIG[pipewire-alsa] = "-Dpipewire-alsa=true,-Dpipewire-alsa=false,alsa-lib"
# pulseaudio drop-in replacement library
PACKAGECONFIG[pipewire-pulseaudio] = "-Dpipewire-pulseaudio=true,-Dpipewire-pulseaudio=false,pulseaudio glib-2.0"
# jack drop-in replacement library
PACKAGECONFIG[pipewire-jack] = "-Dpipewire-jack=true,-Dpipewire-jack=false,jack"

# GStreamer plugins
PACKAGECONFIG[gstreamer] = "-Dgstreamer=true,-Dgstreamer=false,glib-2.0 gstreamer${GST_VER} gstreamer${GST_VER}-plugins-base"

# man pages
PACKAGECONFIG[manpages] = "-Dman=true,-Dman=false,libxml-parser-perl-native"

do_install_append() {
    # only install the alsa config file if the alsa-lib plugin has been built
    # this avoids creating the pipewire-alsa package when the pipewire-alsa
    # feature is not enabled
    if [ -d ${D}${libdir}/alsa-lib ]
    then
        mkdir -p ${D}${datadir}/alsa/alsa.conf.d
        install -m 0644 ${S}/pipewire-alsa/conf/50-pipewire.conf ${D}${datadir}/alsa/alsa.conf.d/50-pipewire.conf
    fi
}

PACKAGES =+ "\
    ${PN}-spa-plugins \
    ${PN}-alsa \
    ${PN}-pulseaudio \
    ${PN}-jack \
    ${PN}-config \
    gstreamer${GST_VER}-${PN} \
    lib${PN} \
    lib${PN}-modules \
"

FILES_${PN} = "\
    ${bindir}/pipewire* \
    ${systemd_user_unitdir}/* \
"

FILES_lib${PN} = "\
    ${libdir}/libpipewire-*.so.* \
"

FILES_lib${PN}-modules = "\
    ${libdir}/pipewire-*/* \
"

FILES_${PN}-spa-plugins = "\
    ${bindir}/spa-* \
    ${libdir}/spa/* \
"

FILES_${PN}-alsa = "\
    ${libdir}/alsa-lib/* \
    ${datadir}/alsa/alsa.conf.d/50-pipewire.conf \
"

FILES_${PN}-pulseaudio = "\
    ${libdir}/libpulse*.so.* \
"

FILES_gstreamer${GST_VER}-${PN} = "\
    ${libdir}/gstreamer-${GST_VER}/* \
"

RDEPENDS_lib${PN} += "lib${PN}-modules ${PN}-spa-plugins"

# The default pipewire config.
# Replace in your own package using
# "virtual/pipewire-config"
FILES_${PN}-config = "\
    ${sysconfdir}/pipewire/pipewire.conf \
"
CONFFILES_${PN}-config += "\
    ${sysconfdir}/pipewire/pipewire.conf \
"
RPROVIDES_${PN}-config += "virtual/pipewire-config"

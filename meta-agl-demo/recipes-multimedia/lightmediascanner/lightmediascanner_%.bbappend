# Disable everything but the roygalty-free formats
PACKAGECONFIG = "ogg flac wave m3u pls jpeg png"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://lightmediascanner.service \
            file://plugin-ogg-fix-chucksize-issue.patch \
            file://0002-switch-from-G_BUS_TYPE_SESSION-to-G_BUS_TYPE_SYSTEM.patch \
            file://dbus-lightmediascanner.conf \
           "

CFLAGS_append = " -D_FILE_OFFSET_BITS=64"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "lightmediascanner.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install_append() {
       # Install LMS systemd service
       if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
              install -d ${D}${systemd_system_unitdir}
              install -m 644 -p -D ${WORKDIR}/lightmediascanner.service ${D}${systemd_system_unitdir}/lightmediascanner.service
       fi

       install -d ${D}/etc/dbus-1/system.d
       install -m 0644 ${WORKDIR}/dbus-lightmediascanner.conf ${D}/etc/dbus-1/system.d/org.lightmediascanner.conf
}

FILES_${PN} += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_system_unitdir}/lightmediascanner.service', '', d)} \
    "

EXTRA_OECONF = "--enable-static --with-dbus-services=${datadir}/dbus-1/system-services"
PACKAGECONFIG[mp4] = "--enable-mp4,--disable-mp4,libmp4v2"

# add support MP3 because of the format of music files for AGL CES/ALS2017 Demo
PACKAGECONFIG_append = " id3 mp4"

# add required character sets for id3 tag scanning
RDEPENDS_${PN}_append = " glibc-gconv-utf-16 glibc-gconv-iso8859-1"

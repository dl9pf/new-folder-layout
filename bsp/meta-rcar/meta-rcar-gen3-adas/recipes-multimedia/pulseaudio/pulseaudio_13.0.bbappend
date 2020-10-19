FILESEXTRAPATHS_append := "${THISDIR}/files:"

PR="r2"

SRC_URI_append_rcar-gen3 = " \
    file://pulseaudio.init \
    file://rsnddai0ak4613h.conf \
    file://hifi \
    file://system.pa \
    file://daemon.conf \
    file://pulseaudio-bluetooth.conf \
    file://pulseaudio-ofono.conf \
"

inherit update-rc.d

INITSCRIPT_PACKAGES = "${PN}-server"
INITSCRIPT_NAME_${PN}-server = "pulseaudio"
INITSCRIPT_PARAMS_${PN}-server = "defaults 30"

do_install_append_rcar-gen3() {
    install -d ${D}/etc/init.d
    install -d ${D}/etc/pulse
    install -d ${D}/usr/share/alsa/ucm/rsnddai0ak4613h/

    install -m 0755 ${WORKDIR}/pulseaudio.init ${D}/etc/init.d/pulseaudio

    install -m 0644 ${WORKDIR}/system.pa ${D}/etc/pulse/system.pa
    install -m 0644 ${WORKDIR}/daemon.conf ${D}/etc/pulse/daemon.conf

    install -m 0644 ${WORKDIR}/rsnddai0ak4613h.conf ${D}${datadir}/alsa/ucm/rsnddai0ak4613h/rsnddai0ak4613h.conf
    install -m 0644 ${WORKDIR}/hifi ${D}${datadir}/alsa/ucm/rsnddai0ak4613h/hifi

    install -d ${D}/${sysconfdir}/dbus-1/system.d
    install -m 644 ${WORKDIR}/pulseaudio-bluetooth.conf ${D}/${sysconfdir}/dbus-1/system.d/
    install -m 644 ${WORKDIR}/pulseaudio-ofono.conf ${D}/${sysconfdir}/dbus-1/system.d/
}

FILES_${PN}-server += " \
	    ${datadir}/alsa/ucm \
	    ${datadir}/dbus-1/ \
"

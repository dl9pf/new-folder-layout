FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
    file://0001-utils-add-a-gstreamer-helper-application-for-interco.patch \
    file://bluealsa-gst-helper@.service \
    "

PACKAGECONFIG += "gsthelper"
PACKAGECONFIG[gsthelper] = "--enable-gsthelper, --disable-gsthelper, gstreamer1.0"

do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        # install the service file
        mkdir -p ${D}${systemd_system_unitdir}/
        install -m 0644 ${WORKDIR}/bluealsa-gst-helper@.service ${D}${systemd_system_unitdir}/bluealsa-gst-helper@.service

        # enable the helper to start together with afm-user-session
        mkdir -p ${D}${systemd_system_unitdir}/afm-user-session@.target.wants
        ln -sf ../bluealsa-gst-helper@.service ${D}${systemd_system_unitdir}/afm-user-session@.target.wants/bluealsa-gst-helper@.service
    fi
}

PACKAGES =+ "${PN}-pipewire"

FILES_${PN}-pipewire = "\
    ${bindir}/bluealsa-gst-helper \
    ${systemd_system_unitdir}/bluealsa-gst-helper@.service \
    ${systemd_system_unitdir}/afm-user-session@.target.wants/bluealsa-gst-helper@.service \
    "
RDEPENDS_${PN}-pipewire += "\
    bluez-alsa \
    pipewire \
    gstreamer1.0-plugins-base \
    gstreamer1.0-pipewire \
    "

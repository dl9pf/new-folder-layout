FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

# NOTE:
#    file://0003-compositor-drm-introduce-drm_get_dmafd_from_view.patch
#    has been removed until someone more familiar with weston internals
#    and waltham can take a look and update it.
SRC_URI_append = "\
    file://0001-Allow-regular-users-to-launch-Weston_7.0.0.patch \
    file://0002-ivi-shell-Fix-crash-due-no-transmitter-screen.patch \
    file://0001-libweston-Expose-weston_output_damage-in-libweston.patch \
    file://0004-main-change-remoting-initialization-timing.patch \
    file://smack-weston \
    "

EXTRA_OEMESON_append = " -Denable-user-start=true"

# Workaround for incorrect upstream definition
PACKAGECONFIG[remoting] = "-Dremoting=true,-Dremoting=false,gstreamer1.0 gstreamer1.0-plugins-base"
PACKAGECONFIG_append = "${@bb.utils.contains('DISTRO_FEATURES', 'weston-remoting', ' remoting', '', d)}"

do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'true', 'false', d)}; then
        # Install SMACK rules
        install -D -m 0644 ${WORKDIR}/smack-weston ${D}${sysconfdir}/smack/accesses.d/weston
    fi
}

FILES_${PN} += "\
    ${sysconfdir}/smack/accesses.d/* \
"

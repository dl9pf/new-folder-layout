FILESEXTRAPATHS_prepend := '${THISDIR}/${PN}-${PV}:'

SRC_URI_append = " \
    file://0001-Allow-to-boot-without-input-device.patch \
    file://0002-Share-toytoolkit-lib.patch \
    file://0003-add-window_set_fullscreen_at_output.patch \
    file://0004-Add-display_poll-function.patch \
    file://0005-Add-wl-ivi-shell-surface-creating-support.patch \
    file://0006-Add-widget_set_surface_allocation-func.patch \
    file://0007-Add-call-for-setting-fullscreen-with-IVI.patch \
"

FILES_${PN} += " ${libdir}/libweston-toytoolkit*"

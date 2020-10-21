FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append="${@bb.utils.contains_any("MACHINE", "salvator-x m3ulcb h3ulcb m3ulcb-nogfx", " "," file://0001-gst-wayland-Install-wayland-header-from-gstwayland-l.patch", d)}"

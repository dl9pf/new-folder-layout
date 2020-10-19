# This patch correct a bug in libva1_1.7.0.bb 1.8 inmeta-intel (no clue when it will be fixed)
# libva.bb calls for an x11 runtime dependency even if wayland is selected
#
RDEPENDS_${PN}-egl_remove = "${@bb.utils.contains("DISTRO_FEATURES", "x11", "", "${PN}-x11", d)}"

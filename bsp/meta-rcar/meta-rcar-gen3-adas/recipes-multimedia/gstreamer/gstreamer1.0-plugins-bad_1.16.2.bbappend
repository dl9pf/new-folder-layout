FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI_append = " \
    file://0001-install-wayland.h-header.patch \
    file://0002-pkgconfig-libgstwayland.patch \
    file://0003-gstkmssink-add-rcar-du-to-driver-list.patch \
"

PACKAGECONFIG_append = " kms"

PACKAGECONFIG_remove = '${@ "vulkan" if not ('opengl' in '${DISTRO_FEATURES}') else ""}'
DEPENDS_remove = '${@ "weston" if not ('wayland' in '${DISTRO_FEATURES}') else ""}'
RDEPENDS_gstreamer1.0-plugins-bad_remove = '${@bb.utils.contains("DISTRO_FEATURES", "wayland opengl", "", "libwayland-egl", d)}'

require conf/include/agl_rcar-nogfx.inc
SOC_FAMILY = "r8a7796"
BOARD_NAME = "m3ulcb"

DTB_SUFFIX = "r8a7796-m3ulcb"

# Mask graphic Pkgs
BBMASK += "gles-user-module|kernel-module-gles|wayland-kms|libgbm"
# Mask MMP recipes
BBMASK += "kernel-module-uvcs-drv|omx-user-module"

DISTRO_FEATURES_append = "${@bb.utils.contains('AGL_XEN_WANTED', '1', ' xen', '', d)}"
IMAGE_INSTALL_append = "${@bb.utils.contains('AGL_XEN_WANTED', '1', ' xen-base xen-mkimage', '', d)}"


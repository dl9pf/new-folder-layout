require conf/include/agl_rcar-nogfx.inc
SOC_FAMILY = "r8a7795"
BOARD_NAME = "h3ulcb"

DTB_SUFFIX = "r8a7795-h3ulcb"

# Mask graphic Pkgs
BBMASK += "gles-user-module|kernel-module-gles|wayland-kms|libgbm"
# Mask MMP recipes
BBMASK += "kernel-module-uvcs-drv|omx-user-module"

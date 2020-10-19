require conf/include/agl_rcar-nogfx.inc
SOC_FAMILY = "r8a7795"
BOARD_NAME = "h3-salvator-x"

DTB_SUFFIX = "r8a7795-salvator-xs"

# Mask graphic Pkgs
BBMASK += "gles-user-module|kernel-module-gles|wayland-kms|libgbm"
# Mask MMP recipes
BBMASK += "kernel-module-uvcs-drv|omx-user-module"

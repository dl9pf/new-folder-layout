require conf/include/agl_rcar.inc
SOC_FAMILY = "r8a7795"
BOARD_NAME = "h3-salvator-x"

DTB_SUFFIX = "r8a7795-salvator-xs"

IMAGE_INSTALL_append_rcar-gen3 = " \
    kernel-module-vspmif \
"

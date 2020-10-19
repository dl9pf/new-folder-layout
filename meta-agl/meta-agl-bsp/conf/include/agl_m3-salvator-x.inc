require conf/include/agl_rcar.inc
SOC_FAMILY = "r8a7796"
BOARD_NAME = "m3-salvator-x"

DTB_SUFFIX = "r8a7796-salvator-xs"

IMAGE_INSTALL_append_rcar-gen3 = " \
    kernel-module-vspmif \
"

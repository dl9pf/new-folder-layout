require conf/include/agl_rcar.inc
SOC_FAMILY = "r8a77990"
BOARD_NAME = "ebisu"

DTB_SUFFIX = "r8a77990-ebisu"

DISTRO_FEATURES_remove = " use_eva_pkg"

# Configuration for USB 3.0
MACHINE_FEATURES_append = " usb3"

IMAGE_INSTALL_append = " gstreamer1.0-omx gstreamer1.0-plugin-vspfilter"
IMAGE_INSTALL_append = " kernel-module-mmngr kernel-module-mmngrbuf kernel-module-uvcs-drv kernel-module-vspmif"

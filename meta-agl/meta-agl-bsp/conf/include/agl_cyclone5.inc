OSTREE_BOOTLOADER ?= "u-boot"

# DEFAULTTUNE for cortex-a9
DISTRO_FEATURES_append = " agl-medium-arm-compiler"

# Use the AGL default ext4.xz to generate the rpi-sdimg
SDIMG_ROOTFS_TYPE = "ext4.xz"

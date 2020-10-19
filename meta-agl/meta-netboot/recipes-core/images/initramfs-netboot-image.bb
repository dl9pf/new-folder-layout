# Netboot initramfs image.
DESCRIPTION = "Netboot initrd image"

PACKAGE_INSTALL = "initramfs-netboot busybox base-passwd ${ROOTFS_BOOTSTRAP_INSTALL}"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

export IMAGE_BASENAME = "initramfs-netboot-image"
IMAGE_LINGUAS = ""

LICENSE = "MIT"

IMAGE_FSTYPES := "${NETBOOT_FSTYPES}"
inherit core-image

# hotfix for dragonboard (which uses _append in the bsp) 
IMAGE_FSTYPES_remove += "wic.gz"

IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "256"

BAD_RECOMMENDATIONS += "busybox-syslog"

INHERIT += "netboot"
OVERRIDES .= ":netboot"

# add 512MB of extra space in ext4 output image
IMAGE_ROOTFS_EXTRA_SPACE = "524288"

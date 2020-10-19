IMAGE_LINGUAS = " "

inherit core-image

IMAGE_INSTALL = "${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_ROOTFS_SIZE ?= "8192"

# Allow extra IMAGE_FSTYPES to be added by boards configs
IMAGE_FSTYPES += "${AGL_EXTRA_IMAGE_FSTYPES}"

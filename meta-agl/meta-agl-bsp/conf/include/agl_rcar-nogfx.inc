DISTRO_FEATURES_append = " sota"

OSTREE_KERNEL = "Image"
IMAGE_BOOT_FILES_sota = "renesas-ota-bootfiles/* ${DTB_SUFFIX}.dtb"
OSTREE_BOOTLOADER ?= "u-boot"

WKS_FILE = "singlepart-noloader.wks"

RENESAS_DATADIR = "/usr"

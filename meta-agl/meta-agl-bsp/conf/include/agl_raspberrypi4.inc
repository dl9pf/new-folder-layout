# Maximum memory allowed to be assigned
GPU_MEM = "256"

# Add CMA to the kernel arguments for SOTA
OSTREE_KERNEL_ARGS_sota_append = " cma=256M"

# use u-boot always
RPI_USE_U_BOOT = "1"

# For libomxil
#LICENSE_FLAGS_WHITELIST = "commercial"

IMAGE_INSTALL_append = " kernel-modules linux-firmware-rpidistro-bcm43430 linux-firmware-rpidistro-bcm43455 create-combined-dtb "

# Psplash causes crash on first boot on RPi
IMAGE_FEATURES_remove = "splash"

# Build updatable image. Only takes effect when sota.bbclass is inherited
DISTRO_FEATURES_append = " sota"

# Add xen build, if the xen feature activated
DISTRO_FEATURES_append = " ${@bb.utils.contains('AGL_XEN_WANTED', '1', ' xen', '', d)}"

# add xen-tools to build
IMAGE_INSTALL_append = " ${@bb.utils.contains('AGL_XEN_WANTED', '1', 'xen-tools xen-mkimage-rpi4', '', d)}"

# Add xen-raspberry binary to boot files
IMAGE_BOOT_FILES_append = " ${@bb.utils.contains('AGL_XEN_WANTED','1', 'xen-${MACHINE}', '',d)}"

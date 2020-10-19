# Maximum memory allowed to be assigned
GPU_MEM = "256"

# Add CMA to the kernel arguments for SOTA
OSTREE_KERNEL_ARGS_sota_append = " cma=256M"

#MULTI_PROVIDER_WHITELIST += "virtual/libgl virtual/egl virtual/libgles1 virtual/libgles2"

# use u-boot always
RPI_USE_U_BOOT = "1"

#RPI_KERNEL_DEVICETREE_remove_raspberrypi3 = "bcm2708-rpi-0-w.dtb"
#RPI_KERNEL_DEVICETREE_OVERLAYS_remove_raspberrypi3 = "overlays/lirc-rpi.dtbo"
#RPI_KERNEL_DEVICETREE_OVERLAYS_append_raspberrypi3 = " overlays/vc4-fkms-v3d.dtbo"

MACHINE_FEATURES += "vc4graphics"

# For libomxil
#LICENSE_FLAGS_WHITELIST = "commercial"

IMAGE_INSTALL_append = " kernel-modules linux-firmware-rpidistro-bcm43430 linux-firmware-rpidistro-bcm43455 create-combined-dtb "

# Psplash causes crash on first boot on RPi
IMAGE_FEATURES_remove = "splash"

# Build updatable image. Only takes effect when sota.bbclass is inherited
DISTRO_FEATURES_append = " sota"

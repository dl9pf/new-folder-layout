
#-------------------------------------------------
## Graphics section ##
#-------------------------------------------------
PACKAGES_GFX_${MACHINE} = "ti-sgx-ddk-km"

# Enable Gfx Pkgs
MACHINE_FEATURES_append = " sgx"
MULTI_PROVIDER_WHITELIST += "virtual/libgl virtual/egl virtual/libgles1 virtual/libgles2"

DEPENDS_remove = "virtual/libgl"

# Preferred providers
PREFERRED_PROVIDER_virtual/libgles1 = ""
PREFERRED_PROVIDER_virtual/libgles2 = "ti-sgx-ddk-um"
PREFERRED_PROVIDER_virtual/egl      = "ti-sgx-ddk-um"
PREFERRED_PROVIDER_virtual/libgbm   = "ti-sgx-ddk-um"
PREFERRED_PROVIDER_virtual/mesa     = "mesa-gl"

#-------------------------------------------------
## Multimedia section ##
#-------------------------------------------------

PACKAGES_MULTIMEDIA   = " \
    ipumm-fw \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pulseaudio', 'pulseaudio-misc' , '', d)}      \
"

PREFERRED_PROVIDER_virtual/kernel  = "linux-ti-staging"
PREFERRED_VERSION_virtual/kernel  = "4.9"

# Distribution-specific runtime components
IMAGE_INSTALL_append = " \
     ${PACKAGES_GFX}     \
     ${PACKAGES_MULTIMEDIA}     \
     cmem \
"

# Include WIC support based on beaglebone
do_image_wic[depends] += "mtools-native:do_populate_sysroot dosfstools-native:do_populate_sysroot"
WKS_FILE = "sdimage-bootpart-uuid.wks"
IMAGE_BOOT_FILES = "MLO u-boot.img uEnv.txt"

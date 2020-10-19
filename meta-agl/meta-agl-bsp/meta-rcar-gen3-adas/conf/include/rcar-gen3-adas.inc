#Keep this as a reminder
#MACHINEOVERRIDES .= ":kingfisher"
#PACKAGE_EXTRA_ARCHS_append = " kingfisher"

PREFERRED_RPROVIDER_virtual/gpsd-conf ?= "gpsd-kingfisher-conf"

IMAGE_NAME = "${IMAGE_BASENAME}-${MACHINE}-kf${IMAGE_VERSION_SUFFIX}"
IMAGE_LINK_NAME = "${IMAGE_BASENAME}-${MACHINE}-kf"
TOOLCHAIN_OUTPUTNAME = "${SDK_NAME}-kf-toolchain-${SDK_VERSION}"

# Radio packages
IMAGE_INSTALL_append_rcar-gen3 += " \
    si-tools \
    si-init \
    linux-firmware-wl18xx \
    ti-bt \
    ti-bt-firmware \
"

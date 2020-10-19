SUMMARY = "The software for AGL IVI DEMO profile"
DESCRIPTION = "A set of packages belong to AGL Demo"

LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-demo \
    "

ALLOW_EMPTY_${PN} = "1"

# MOST out-of-tree kernel drivers
#################################
MOST_DRIVERS ??= " \
    most \
    "
# These boards use different kernels - needs to be checked
MOST_DRIVERS_dragonboard-410c ?= ""
MOST_DRIVERS_cyclone5 ?= ""


# HVAC dependencies
###################
LIN_DRIVERS ??= " sllin"
# These boards use different kernels - needs to be checked
LIN_DRIVERS_dra7xx-evm ?= ""
LIN_DRIVERS_dragonboard-410c ?= ""

# UNICENS service
UNICENS ?= " \
    unicens-config \
    agl-service-unicens \
    agl-service-unicens-controller \
    "

# Hook for demo platform configuration
# ATM, only used to disable btwilink module on [MH]3ULCB + Kingfisher by default,
# setting DEMO_ENABLE_BTWILINK to "true" in local.conf / site.conf re-enables.
DEMO_ENABLE_BTWILINK ?= ""
DEMO_PLATFORM_CONF = ""
DEMO_PLATFORM_CONF_append_ulcb = "${@bb.utils.contains("DEMO_ENABLE_BTWILINK", "true", "", " btwilink-disable-conf", d)}"

RDEPENDS_${PN} += "\
    udisks2 \
    "

# fonts
TTF_FONTS = " \
    ttf-bitstream-vera \
    ttf-dejavu-sans \
    ttf-dejavu-sans-mono \
    ttf-dejavu-serif \
    ttf-noto-emoji-color \
    source-han-sans-cn-fonts \
    source-han-sans-jp-fonts \
    source-han-sans-tw-fonts \
    "


RDEPENDS_${PN} += " \
    linux-firmware-ath9k \
    linux-firmware-ralink \
    can-utils \
    iproute2 \
    dhcp-client \
    ${UNICENS} \
    ${MOST_DRIVERS} \
    ${LIN_DRIVERS} \
    ${DEMO_PLATFORM_CONF} \
    ${TTF_FONTS} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'webruntime', 'virtual/webruntime', '', d)} \
    "

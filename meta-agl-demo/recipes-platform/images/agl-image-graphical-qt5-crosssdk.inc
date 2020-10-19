require recipes-platform/images/agl-image-minimal-crosssdk.inc

TOOLCHAIN_TARGET_TASK += "libafb-helpers-qt-staticdev"

# Add wayland-scanner to SDK (SPEC-945)
# Use TOOLCHAIN_HOST_TASK instead of adding to the packagegroup
# wayland-scanner is in nativesdk-wayland-dev !
# option: add also nativesdk-qtwayland-tools
TOOLCHAIN_HOST_TASK_append = " nativesdk-wayland nativesdk-wayland-dev"

TOOLCHAIN_HOST_TASK_append = " nativesdk-perl-modules "

# Add qtwaylandscanner to the SDK
TOOLCHAIN_HOST_TASK_append = " nativesdk-qtwayland-tools "

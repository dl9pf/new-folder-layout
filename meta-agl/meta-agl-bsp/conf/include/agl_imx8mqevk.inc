include agl_imx8mqevk-common.inc

# NOTE: currently not using IMX_DEFAULT_BSP = "mainline", as it
#       requires more changes than this approach of disabling
#       Vivante.

# Add our own over-ride for use in a few recipes where there is
# conflicting Vivante vs etnaviv configuration.
MACHINEOVERRIDES .= ":etnaviv"

# Knock out Vivante overrides
MACHINEOVERRIDES_EXTENDER_mx8mq_forcevariable = ""
PREFERRED_VERSION_weston_mx8 = "8.0.0"
PREFERRED_VERSION_wayland-protocols_mx8 = "1.20"
PREFERRED_VERSION_libdrm_mx8 = "2.4.100"

CORE_IMAGE_EXTRA_INSTALL += "libdrm-etnaviv"

PREFERRED_VERSION_gstreamer1.0_mx8 = "1.16.2"
PREFERRED_VERSION_gstreamer1.0-plugins-base_mx8 = "1.16.2"
PREFERRED_VERSION_gstreamer1.0-plugins-good_mx8 = "1.16.2"
PREFERRED_VERSION_gstreamer1.0-plugins-bad_mx8 = "1.16.2"
PREFERRED_VERSION_gstreamer1.0-plugins-ugly_mx8 = "1.16.2"

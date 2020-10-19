########################
# AGL DISTRO VARIABLES #
########################

# ARM 32bit 'medium' compiler (armv7thf instead of armv7ve)
# (only use for CPUs <= cortex-a9 / w/o virt extension - e.g. cortex-a8)
# AGL_FEATURES_append = " agl-medium-arm-compiler"


######################
# AGL USER VARIABLES #
######################

# Add additional IMAGE_FSTYPES here on-top of the default ext4.xz and tar.xz
#AGL_EXTRA_IMAGE_FSTYPES = ""

# Additional free disk space created in the image in Kbytes.
#IMAGE_ROOTFS_EXTRA_SPACE="524288"

# meta-agl/meta-agl-profile-graphical/recipes-graphics/wayland/weston-init.bbappend
# AGL's compositor is started instead of weston like so
# AGL_DEFAULT_WESTONSTART ?= "/usr/bin/agl-compositor --config ${sysconfdir}/xdg/weston/weston.ini"
# you can redefine it using this variable to go back to weston,
# but be careful that this needs further changes to work !
# Especially the AGL demo apps will not work as-is.
#AGL_DEFAULT_WESTONSTART = "/usr/bin/weston"

# meta-agl-profile-graphical-qt5/recipes-qt/qt5/qtwayland_%.bbappend
# This defines the QT integration. We default to xdg-shell
# AGL_DEFAULT_WM_SHELL ?= "xdg-shell"
# you can redefine it using this variable to e.g. use ivi-shell
# but be careful that this needs further changes to work !
#AGL_DEFAULT_WM_SHELL = ""

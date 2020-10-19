require recipes-platform/images/agl-image-boot.inc

IMAGE_FEATURES += "splash package-management ssh-server-dropbear"

inherit features_check

REQUIRED_DISTRO_FEATURES = "wayland"

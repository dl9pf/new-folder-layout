require recipes-platform/images/agl-image-minimal.inc

IMAGE_INSTALL_append = "\
    packagegroup-agl-image-ivi \
    packagegroup-agl-ivi-services \
    agl-service-homescreen \
    agl-service-weather \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pipewire', 'pipewire', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pipewire', 'agl-service-mediaplayer', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pipewire', 'agl-service-radio', '', d)} \
    can-utils \
    iproute2 \
    "

IMAGE_FEATURES += "splash package-management ssh-server-dropbear"
DISTRO_FEATURES_append = " agl-core-image-profile"

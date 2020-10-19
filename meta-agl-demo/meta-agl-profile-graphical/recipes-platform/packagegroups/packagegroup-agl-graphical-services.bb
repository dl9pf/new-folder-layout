DESCRIPTION = "The minimal set of packages for Connectivity Subsystem"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-graphical-services \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'pipewire', 'agl-service-mediaplayer', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pipewire', 'agl-service-radio', '', d)} \
    "

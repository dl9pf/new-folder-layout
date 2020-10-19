SUMMARY = "The software for application framework of AGL IVI profile"
DESCRIPTION = "A set of packages belong to AGL application framework which required by \
Multimedia Subsystem"

LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-graphical-multimedia \
    "

RDEPENDS_${PN} += "\
    alsa-utils \
    gstreamer1.0-meta-base \
    "

SUMMARY = "The middleware for AGL IVI profile"
DESCRIPTION = "The set of packages required for AGL IVI Distribution"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-profile-graphical \
    profile-graphical \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    packagegroup-agl-image-minimal \
    packagegroup-agl-graphical-weston \
"

RDEPENDS_profile-graphical = "${PN}"

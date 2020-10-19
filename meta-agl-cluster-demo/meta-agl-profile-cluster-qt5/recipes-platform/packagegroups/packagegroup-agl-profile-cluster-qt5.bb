SUMMARY = "The middleware for AGL Qt5 based cluster profile"
DESCRIPTION = "The set of packages required for AGL Qt5 based Cluster Distribution"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-profile-cluster-qt5 \
    profile-cluster-qt5 \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    packagegroup-agl-image-boot \
    packagegroup-agl-core-security \
    packagegroup-agl-graphical-weston \
    packagegroup-agl-appfw-native-qt5 \
"

RDEPENDS_profile-cluster-qt5 = "${PN}"

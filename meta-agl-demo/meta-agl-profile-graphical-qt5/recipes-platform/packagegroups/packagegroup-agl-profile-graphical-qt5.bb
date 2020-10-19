SUMMARY = "The middlewares for AGL IVI profile"
DESCRIPTION = "The set of packages required for AGL Distribution"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-profile-graphical-qt5 \
    profile-graphical-qt5 \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    packagegroup-agl-profile-graphical \
    packagegroup-agl-appfw-native-qt5 \
"

RDEPENDS_${PN} += "\
    agl-login-manager \
    "

RDEPENDS_profile-graphical-qt5 = "${PN}"

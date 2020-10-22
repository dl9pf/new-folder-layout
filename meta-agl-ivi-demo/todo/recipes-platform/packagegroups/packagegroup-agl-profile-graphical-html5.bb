SUMMARY = "AGL web runtime profile"
DESCRIPTION = "The full set of packages required for AGL web runtime"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-profile-graphical-html5 \
    profile-graphical-html5 \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    packagegroup-agl-profile-graphical \
    packagegroup-agl-appfw-html5 \
"

RDEPENDS_${PN} += "\
    agl-login-manager \
    "

RDEPENDS_profile-graphical-html5 = "${PN}"

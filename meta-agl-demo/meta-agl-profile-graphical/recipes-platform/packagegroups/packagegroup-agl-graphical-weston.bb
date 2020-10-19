DESCRIPTION = "The minimal set of packages required for Wayland support"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-graphical-weston \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += " \
                  weston \
                  weston-init \
                  weston-ini-conf \
                  weston-examples \
                  agl-login-manager \
                  agl-compositor \
                  "


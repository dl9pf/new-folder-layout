DESCRIPTION = "Packages for the Open IoT Service Platform."
LICENSE = "MIT"

inherit packagegroup

PR = "r0"

PACKAGES = "${PN}"

PACKAGECONFIG ??= "cli python"

PACKAGECONFIG[cli] = "\
    , \
    , \
    , \
    oisp-cli \
"

PACKAGECONFIG[python] = "\
    , \
    , \
    , \
    python3-oisp \
"

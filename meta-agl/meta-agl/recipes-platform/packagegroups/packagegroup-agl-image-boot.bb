DESCRIPTION = "The minimal set of packages required by AGL"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-image-boot \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    packagegroup-agl-core-boot \
    packagegroup-machine-base \
    "

RDEPENDS_${PN} += "\
    "

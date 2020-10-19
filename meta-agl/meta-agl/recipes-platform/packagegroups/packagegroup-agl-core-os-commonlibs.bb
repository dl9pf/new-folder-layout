SUMMARY = "The packages of middlewares for AGL IVI profile"
DESCRIPTION = "The set of packages required by Operating System and Common libraries Subsystem"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-core-os-commonlibs \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    "

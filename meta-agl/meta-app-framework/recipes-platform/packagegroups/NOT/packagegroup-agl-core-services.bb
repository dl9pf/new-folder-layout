DESCRIPTION = "The minimal set of packages for AGL core Connectivity Subsystem"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-core-services \
    "

RDEPENDS_${PN} += "\
    agl-service-data-persistence \
    agl-service-network \
    agl-service-platform-info \
    "

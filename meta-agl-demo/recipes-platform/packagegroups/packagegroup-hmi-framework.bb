SUMMARY = "The software for the AGL hmi framework 2017"
DESCRIPTION = "A set of packages belong to the hmi framework 2017"

LICENSE = "MIT"

# need to bump manually due to:
# - nothing provides libqthomescreenwrapper0 needed by packagegroup-hmi-framework-1.0-r0.noarch
PR = "2"

inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES = "\
    packagegroup-hmi-framework \
    packagegroup-hmi-framework-devel \
    "

RDEPENDS_${PN} = " \
  agl-service-homescreen \
  homescreen \
  libhomescreen \
  qlibhomescreen \
  hmi-debug \
  launcher \
"

RDEPENDS_append_${PN} = " ${@bb.utils.contains('DISTRO_FEATURES', 'AGLCI', 'homescreen-demo-ci', '', d)}"

# NOTE: Currently no coverage versions of these widgets, they should
#       be added here when available.
RDEPENDS_${PN}-devel = " \
  agl-service-homescreen-dbg \
  homescreen-dbg \
  launcher-dbg \
"

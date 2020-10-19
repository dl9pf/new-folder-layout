require libafb-helpers_git.inc

inherit cmake

RDEPENDS_${PN}_append = " af-binder"

ALLOW_EMPTY_${PN} = "1"


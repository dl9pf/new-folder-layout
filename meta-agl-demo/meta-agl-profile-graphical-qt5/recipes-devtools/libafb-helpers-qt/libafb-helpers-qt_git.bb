require recipes-devtools/libafb-helpers/libafb-helpers_git.inc

DEPENDS_append = " qtwebsockets"
RDEPENDS_${PN}_append = " af-binder"

inherit cmake_qt5

EXTRA_OECMAKE_append = " -DAFB_HELPERS_QT=ON -DAFB_HELPERS=OFF"

ALLOW_EMPTY_${PN} = "1"


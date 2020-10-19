require af-binder_${PV}.inc

DEPENDS = "json-c-native"

inherit cmake pkgconfig native

EXTRA_OECMAKE_append = " -DONLY_DEVTOOLS=TRUE"


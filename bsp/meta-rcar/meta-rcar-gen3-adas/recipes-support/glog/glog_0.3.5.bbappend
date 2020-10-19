FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI_append = " file://0001-Use-pkg-config-for-locating-gflags-and-gmock.patch"

DEPENDS += "gflags gmock gtest"

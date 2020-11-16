DESCRIPTION = "pytest-reverse is a pytest plugin to reverse test order"
HOMEPAGE = "https://github.com/adamchainz/pytest-reverse"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=caf1f407ae86ecd57ab721dff94079b7"

SRC_URI[sha256sum] = "40cbc47df8a262fed778e500f4d0b17d2d08ef8b9fbf899c0bab9488be192aac"

inherit pypi setuptools3

DEPENDS += "${PYTHON_PN}-pytest-native"

BBCLASSEXTEND = "native"

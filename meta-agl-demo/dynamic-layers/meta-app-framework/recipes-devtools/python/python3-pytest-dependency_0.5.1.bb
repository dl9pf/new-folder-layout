DESCRIPTION = "pytest-dependency manages dependencies of tests."
HOMEPAGE = "https://github.com/RKrahl/pytest-dependency"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://README.rst;md5=35b9938ae48d25e6b8306232e98463dd"

SRC_URI[sha256sum] = "c2a892906192663f85030a6ab91304e508e546cddfe557d692d61ec57a1d946b"

inherit pypi setuptools3

DEPENDS += "${PYTHON_PN}-pytest-native"

BBCLASSEXTEND = "native"

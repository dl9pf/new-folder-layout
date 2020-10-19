DESCRIPTION = "yaml-cpp is a YAML parser and emitter in C++ matching the YAML 1.2 spec"
HOMEPAGE = "https://github.com/jbeder/yaml-cpp/"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a8aaf0595c2efc1a9c2e0913e9c1a2c"

DEPENDS = "boost"

PR = "r0"

S = "${WORKDIR}/git"

SRC_URI = "git://github.com/jbeder/${BPN}.git"

# tag: yaml-cpp-0.6.2
SRCREV = "562aefc114938e388457e6a531ed7b54d9dc1b62"

inherit cmake

EXTRA_OECMAKE += "-DBUILD_SHARED_LIBS=ON -DYAML_CPP_BUILD_TESTS=OFF"

FILES_${PN}-dev += "${libdir}/cmake"


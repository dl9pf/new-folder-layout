SUMMARY = "ScanCode toolkit"
DESCRIPTION = "A typical software project often reuses hundreds of third-party \
packages. License and origin information is not always easy to find and not \
normalized: ScanCode discovers and normalizes this data for you."
HOMEPAGE = "https://github.com/nexB/scancode-toolkit"
SECTION = "devel"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=8aedb84647f637c585e71f8f2e96e5c8"

inherit native

DEPENDS = "xz-native zlib-native libxml2-native \
	   libxslt-native bzip2-native \
           "

SRC_URI = "git://github.com/nexB/scancode-toolkit;branch=develop \
          "

SRCREV = "4a5c596a2f02bb69e7764a8e2641286f5625d85e"

S = "${WORKDIR}/git"
B = "${S}"

export PYTHON_EXE="${HOSTTOOLS_DIR}/python3"

do_configure(){
	./scancode --help
}

do_install_append(){
	install -d ${D}${bindir}/bin
	install -d ${D}${bindir}/include
	install -d ${D}${bindir}/local

	install ${S}/scancode ${D}${bindir}/
	cp -rf ${S}/bin/* ${D}${bindir}/bin/
	cp -rf ${S}/include/* ${D}${bindir}/include/
}


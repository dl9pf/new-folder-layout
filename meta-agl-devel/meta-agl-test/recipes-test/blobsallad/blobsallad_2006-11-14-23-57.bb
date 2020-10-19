DESCRIPTION = ""
HOMEPAGE = "http://blobsallad.se/"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

SRC_URI = " \
    http://blobsallad.se/src/blobsallad-src-${PV}.tar.gz \
    file://0001-Makefile.patch \
    file://0002-auto.patch \
    file://0003-printcleanup.patch \
    file://0004-bs_main.c.patch"

SRC_URI[md5sum] = "a2e3342cbf0f3a4a9b110af2663bed36"
SRC_URI[sha256sum] = "cd6309df1929d3e6b7bce1dbecc751849aeaafe17c01e05fd2567dc4267faaa2"

DEPENDS = "cairo virtual/libsdl"

INSANE_SKIP_${PN} = "ldflags"

S = "${WORKDIR}/blobsallad-src"

do_configure () {
	# Specify any needed configure commands here
	:
}

do_compile () {
	# You will almost certainly need to add additional arguments here
	oe_runmake SDKROOT=${STAGING_DIR_HOST}
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 blobsallad ${D}${bindir}
}


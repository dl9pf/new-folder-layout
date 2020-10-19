FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://psplash-colors.h"

SPLASH_IMAGES="file://psplash-poky-img.h;outsuffix=default"

do_configure_append () {
	cp -f ${WORKDIR}/psplash-colors.h ${S}
}

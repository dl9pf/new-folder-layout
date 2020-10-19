SUMMARY = "AGL gcovr wrapper"
DESCRIPTION = "This wrapper script enables running gcovr against a \
AGL binding to generate a coverage report of running pyagl tests, \
the afm-test test widget, or a user-supplied command."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI += "file://gcovr-wrapper"

inherit allarch

do_install() {
    install -D -m 0755 ${WORKDIR}/gcovr-wrapper ${D}${bindir}/gcovr-wrapper
}

RDEPENDS_${PN} = "bash gcovr"

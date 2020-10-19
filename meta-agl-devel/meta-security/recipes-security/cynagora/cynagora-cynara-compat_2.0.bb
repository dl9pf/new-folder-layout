DESCRIPTION = "Cynara service with client libraries"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://Apache-2.0;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/cynagora;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "6c88efcb7b1361ba6389753e520e26fc556b7d79"
PV = "2.0+git${SRCPV}"

S = "${WORKDIR}/git"

inherit cmake

PROVIDES = "cynara"
RPROVIDES_${PN} = "cynara"
DEPENDS = "libcap"
RDEPENDS_${PN} = "cynagora"

EXTRA_OECMAKE += " \
	-DWITH_SYSTEMD=OFF \
	-DWITH_CYNARA_COMPAT=ON \
	-DDIRECT_CYNARA_COMPAT=ON \
"

do_install_append() {
	# remove cynagora stuff
	rm $(find ${D} -name '*cynagora*')
	# remove stupid test
	rm -r ${D}${bindir}
}


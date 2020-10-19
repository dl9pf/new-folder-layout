DESCRIPTION = "Cynagora service and client libraries"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://Apache-2.0;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/cynagora;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "218dad2eddcbedaede44753e64ea7c30b73b00aa"
PV = "2.0+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "systemd libcap"

inherit cmake

EXTRA_OECMAKE += " \
	-DSYSTEMD_UNIT_DIR=${systemd_system_unitdir} \
	-DWITH_SYSTEMD=ON \
	-DWITH_CYNARA_COMPAT=OFF \
"

inherit useradd
USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "-r cynagora"
USERADD_PARAM_${PN} = "\
--system --home ${localstatedir}/lib/empty \
--no-create-home --shell /bin/false \
--gid cynagora cynagora \
"

FILES_${PN} += "${systemd_system_unitdir}"

PACKAGES =+ "${PN}-tools"
FILES_${PN}-tools += "${bindir}/cynagora-admin ${bindir}/cynagora-agent"
RDEPENDS_${PN}_append_agl-devel = " ${PN}-tools"

inherit ptest
SRC_URI_append = " file://run-ptest"
RDEPENDS_${PN}-ptest_append = " ${PN}-tools"

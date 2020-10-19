DESCRIPTION = "OAuth server using cynagora backend"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/cynagoauth.git;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "26a5dbddf3a9bfde481a6fcd2aae16c7ecba665f"
PV = "0.1+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "json-c libmicrohttpd openssl cynagora"

inherit cmake

EXTRA_OECMAKE += " \
	-DDEFAULTHOSTS=:7777 \
	-DDEFAULTURL=http://localhost:7777/tok \
	-DUNITDIR_SYSTEM=${systemd_system_unitdir} \
"

FILES_${PN} += "${systemd_system_unitdir}"



HOMEPAGE = "here"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "file://udev-shared.conf"

S = "${WORKDIR}"

do_install() {
	d=${D}${systemd_system_unitdir}/systemd-udevd.service.d
	install -d $d
	install -m 0644 ${S}/udev-shared.conf $d
}

FILES_${PN} = "${systemd_system_unitdir}"

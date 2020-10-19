SUMMARY = "King fisher specific gpsd config"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"

SRC_URI = " \
    file://gpsd.kingfisher \
"

inherit update-alternatives

RPROVIDES_${PN} += "virtual/gpsd-conf"

ALTERNATIVE_${PN} = "gpsd-defaults"
ALTERNATIVE_LINK_NAME[gpsd-defaults] = "${sysconfdir}/default/gpsd"
ALTERNATIVE_TARGET[gpsd-defaults] = "${sysconfdir}/default/gpsd.kingfisher"
ALTERNATIVE_PRIORITY[gpsd-defaults] = "20"

COMPATIBLE_MACHINE = "ulcb"
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -d ${D}/${sysconfdir}/default
    install -m 0644 ${WORKDIR}/gpsd.kingfisher ${D}/${sysconfdir}/default/gpsd.kingfisher
}

FILES_${PN} = "${sysconfdir}/default/gpsd.kingfisher"
CONFFILES_${PN} = "${sysconfdir}/default/gpsd.kingfisher"

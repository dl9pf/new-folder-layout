DESCRIPTION = "Configurations for HMI framework"

SECTION = "HMI"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = " \
    file://hmi-debug \
"

FILES_${PN} = " \
    ${sysconfdir}/afm/unit.env.d \
"

do_install() {
    install -d ${D}${sysconfdir}/afm/unit.env.d
    install -m 644 ${WORKDIR}/hmi-debug ${D}${sysconfdir}/afm/unit.env.d
}

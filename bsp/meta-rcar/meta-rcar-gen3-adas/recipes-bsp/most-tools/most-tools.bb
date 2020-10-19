SUMMARY = "MOST tools"
SECTION = "multimedia"
LICENSE = "CLOSED"

S = "${WORKDIR}/most-tools"

SRC_URI = " \
    file://most-tools.tar.gz \
"

do_install() {
    install -d ${D}/usr/share/most/
    install -m 755 ${S}/most_setup.sh ${D}/usr/share/most/
    install -m 755 ${S}/most_play.sh ${D}/usr/share/most/
    install -m 755 ${S}/setup-audio-50 ${D}/usr/share/most/
}

FILES_${PN} = " \
    /usr/share/most/most_setup.sh \
    /usr/share/most/most_play.sh \
    /usr/share/most/setup-audio-50 \
"

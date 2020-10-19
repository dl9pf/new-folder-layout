SUMMARY = "udev rules for R-Car Gen3 CV Library"
LICENSE = "CLOSED"

SRC_URI = " \
    file://51-imp.rules \
    file://52-cmem.rules \
    file://53-vip.rules \
    file://54-isp.rules \
    file://55-fcpr.rules \
"

do_install () {
    install -d ${D}${sysconfdir}/udev/rules.d/
    install -m 0644 ${WORKDIR}/51-imp.rules ${D}${sysconfdir}/udev/rules.d/
    install -m 0644 ${WORKDIR}/52-cmem.rules ${D}${sysconfdir}/udev/rules.d/
    install -m 0644 ${WORKDIR}/53-vip.rules ${D}${sysconfdir}/udev/rules.d/
    install -m 0644 ${WORKDIR}/54-isp.rules ${D}${sysconfdir}/udev/rules.d/
    install -m 0644 ${WORKDIR}/55-fcpr.rules ${D}${sysconfdir}/udev/rules.d/
}

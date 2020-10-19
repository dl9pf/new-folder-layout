SUMMARY = "Tool to communicate with Broadcom switch"
SECTION = "core"

LICENSE = "CLOSED"

PE = "1"
PV = "0.1"

SRC_URI = " \
    file://br-test.tar.gz \
"

S = "${WORKDIR}/br-test"

do_install() {
    install -d ${D}${bindir}
    
    install -m 755 br_test ${D}${bindir}
}

FILES_${PN} = " \
    ${bindir}/br_test \
"

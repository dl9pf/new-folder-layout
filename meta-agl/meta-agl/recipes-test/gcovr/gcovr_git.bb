SUMMARY = "Generate GCC code coverage reports"
DESCRIPTION = "Gcovr provides a utility for managing the use of the GNU gcov \
utility and generating summarized code coverage results."
HOMEPAGE = "https://gcovr.com"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=221e634a1ceafe02ef74462cbff2fb16"

PV = "4.2+git${SRCPV}"
SRC_URI = "git://github.com/gcovr/gcovr.git;protocol=https"
SRCREV = "1bc72e3bb59b9296e962b350691732ddafbd3195"

S = "${WORKDIR}/git"

inherit setuptools3

RDEPENDS_${PN} += " \
    python3-compression \
    python3-core \
    python3-crypt \
    python3-datetime \
    python3-difflib \
    python3-io \
    python3-jinja2 \
    python3-json \
    python3-lxml \
    python3-multiprocessing \
    python3-pygments \
    python3-pytest \
    python3-shell \
    python3-threading \
    python3-typing \
"

SUMMARY = "Kernel test scripts"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/qa-test-misc;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "e2472c7b1d061aef8bb05a4d7940fd8159c4a329"

inherit allarch

S = "${WORKDIR}/git/${PN}-${PV}"

do_install () {
	for subdir in arch_timer cmt common scifab sdhi.0; do 
		install -d -m 0755 ${D}/${datadir}/${PN}/$subdir
		install -m 0755 -t ${D}/${datadir}/${PN}/$subdir ${S}/$subdir/*
	done
}


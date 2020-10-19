DESCRIPTION = "Benchmark tool that generates a workload resembling common web application server workloads"
HOMEPAGE = "http://ebizzy.sourceforge.net/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = "http://downloads.sourceforge.net/project/ebizzy/ebizzy/${PV}/ebizzy-${PV}.tar.gz"
SRC_URI[md5sum] = "af038bc506066bb3d28db08aba62bc38"
SRC_URI[sha256sum] = "77286029d6348f3d9b3f04eae1feadb5ad1ad07b9f688f4d9f002960862467f2"

INSANE_SKIP_${PN} = "ldflags"

do_compile () {
	${CC} -Wall -Wshadow -lpthread -o ebizzy ebizzy.c
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 ebizzy ${D}${bindir}
}


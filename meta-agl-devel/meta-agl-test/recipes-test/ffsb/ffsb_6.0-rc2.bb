DESCRIPTION = "Flexible File System Benchmark"
HOMEPAGE = "https://sourceforge.net/projects/ffsb/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fd5d9bcabd8ed5a54a01ce8d183d592a"

SRC_URI = "http://downloads.sourceforge.net/project/ffsb/ffsb/ffsb-${PV}/ffsb-${PV}.tar.bz2"
SRC_URI[md5sum] = "cabfc1021c2ec6c6b168fefc84210891"
SRC_URI[sha256sum] = "e5867692aae8c9bfbcdc774599022289c4d89c1d90f4dd7101fb9865ac773c71"

inherit autotools

# extra args to configure
EXTRA_OECONF = ""


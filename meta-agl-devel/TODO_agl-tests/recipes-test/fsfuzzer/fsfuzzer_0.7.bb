SUMMARY = "The fsfuzzer is a filesystem fuzzer tool that does stress tests of various filesystems in a reproducible and logged way."
DESCRIPTION = "This tool creates initial (valid) filesystem images and then manipulates their binary format and structure for detecting flaws/bugs/design problems in the parsing/handling code for that particular filesystem. The program expects that you have a /media directory. It uses that one for mounting test images in."
HOMEPAGE = "https://www.ee.oulu.fi/research/ouspg/fsfuzzer"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "http://people.redhat.com/sgrubb/files/fsfuzzer-${PV}.tar.gz \
           file://0001-fix_missing_header_sys_stat.patch \
	"
SRC_URI[md5sum] = "d69ac4b67aaba52a889c9188eb456c15"
SRC_URI[sha256sum] = "094aa17c4f66c525a04e5db5ad444b4a561b6f8e310d0bd70b9ca421cdf0a434"

inherit autotools

# Specify any options you want to pass to the configure script using EXTRA_OECONF:
EXTRA_OECONF = ""


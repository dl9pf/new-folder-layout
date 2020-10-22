SUMMARY = "Async IO testing utility"
DESCRIPTION = "aio-stress will open or create each file on the command line, and start a series of aio to it.\
aio is done in a rotating loop.  first file1 gets 8 requests, then file2, then file3 etc.\
As each file finishes writing, it is switched to reads io buffers are aligned in case you want to do raw io"
HOMEPAGE = "https://oss.oracle.com/~mason/aio-stress/"
LICENSE = "GPLv2"

S="${WORKDIR}"

LIC_FILES_CHKSUM = "file://${S}/aio-stress.c;md5=ccb5d196a3736bbd835d582a4e2329c3"

SRC_URI = "https://oss.oracle.com/~mason/aio-stress/aio-stress.c"
SRC_URI[md5sum] = "ccb5d196a3736bbd835d582a4e2329c3"
SRC_URI[sha256sum] = "3f4cffcc946fb717fff9d8fe932c7c2ee606efff198408d9fbe16955151445f7"

do_compile () {
	${CC} -Wall -Wshadow -o aio-stress -lpthread -laio aio-stress.c
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 aio-stress ${D}${bindir}
}


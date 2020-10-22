DESCRIPTION = "Himeno Benchmark"
LICENSE = "LGPL-2.0"
URL = "http://accc.riken.jp/en/supercom/himenobmt/"

SRC_URI = "http://accc.riken.jp/en/wp-content/uploads/sites/2/2015/07/himenobmt.c.zip"
SRC_URI[md5sum] = "8d8e837dfc3e00a3b322eb4ae3edd551"
SRC_URI[sha256sum] = "8780bfbdacb0d4dcab2931e65cadd69411731e3f2afcc1231d0743a036298077"

LIC_FILES_CHKSUM = "file://himenoBMT.c;md5=8e8771ddc01c6d1a795e088e2d6dee78"

# Upstream ZIP file contains an LZH archive, thus a 2 steps "unpack" is required.
# The LZH archive is unpacked after native tools dependency resolution,
# thus. after "configure" task in our case:

DEPENDS = "p7zip-native"

do_configure_append() {
    7z x -y -o${S} ${WORKDIR}/himenobmt.c.lzh
    cd ${S}
    sed -i -e 's/CC= gcc/#CC= gcc/' Makefile
    sed -i -e 's/CFLAGS = /#CFLAGS = /' Makefile
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 bmt ${D}${bindir}
}

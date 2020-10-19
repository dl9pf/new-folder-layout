DESCRIPTION = "Waltham is a network IPC library designed to resemble Wayland both protocol and protocol-API wise"
HOMEPAGE = "https://github.com/waltham/waltham"

LICENSE = "MIT"
LIC_FILES_CHKSUM = " \
  file://LICENSE;md5=e8ad01a5182f2c1b3a2640e9ea268264 \
"
SRCREV = "1de58c3ff746ddaba7584d760c5454243723d3ca"
SRC_URI = "git://github.com/wmizuno/waltham.git \
          "
S = "${WORKDIR}/git"

inherit autotools pkgconfig

DEPENDS += "libdrm virtual/kernel wayland"
RDEPENDS_${PN} += "libdrm"
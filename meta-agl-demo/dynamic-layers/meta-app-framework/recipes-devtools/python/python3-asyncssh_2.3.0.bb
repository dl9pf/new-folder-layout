DESCRIPTION = "\
AsyncSSH is a Python package which provides an asynchronous client and \
server implementation of the SSHv2 protocol on top of the Python \
asyncio framework."
HOMEPAGE = "https://github.com/ronf/asyncssh"
LICENSE = "EPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d9fc0efef5228704e7f5b37f27192723"

SRC_URI[sha256sum] = "44bda34c7123f00c3df95d24e2dc8d43c4d17b456fbb8c434ef4f4a7ebb5265e"

inherit pypi setuptools3

RDEPENDS_${PN} += "${PYTHON_PN}-asyncio ${PYTHON_PN}-cryptography"

BBCLASSEXTEND = "native"

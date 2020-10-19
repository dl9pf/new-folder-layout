DESCRIPTION = "AWS IoT device SDK for embedded C"
AUTHOR = "AWS"
HOMEPAGE = "https://github.com/aws/aws-iot-device-sdk-embedded-C"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=acc7a1bf87c055789657b148939e4b40"

SRC_URI = "\
    git://github.com/aws/aws-iot-device-sdk-embedded-C.git;protocol=https \
    file://Makefile.aws \
    file://aws_iot_config.h \
    file://awsiotsdk.pc \
"
SRCREV = "d039f075e1cc2a2a7fc20edc6868f328d8d36b2f"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

DEPENDS = "mbedtls"
RDEPENDS_${PN} += "mbedtls"

do_configure_prepend() {
	cp ${WORKDIR}/Makefile.aws ${S}/src
	cp ${WORKDIR}/aws_iot_config.h ${S}/include
	cp ${WORKDIR}/awsiotsdk.pc ${S}
}

do_compile() {
	cd ${S}/src
	oe_runmake -f ./Makefile.aws DESTDIR=${D} all
}

do_install() {
	cd ${S}/src
	oe_runmake -f ./Makefile.aws DESTDIR=${D} install
}

BBCLASSEXTEND = "native nativesdk"

ALLOW_EMPTY_${PN} = "1"


SUMMARY     = "AGL cloud proxy service"
DESCRIPTION = "AGL cloud proxy service build with recipe method"
HOMEPAGE    = "https://gerrit.automotivelinux.org/gerrit/apps/agl-service-cloudproxy"
SECTION     = "apps"
LICENSE     = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=133bb7bd14f18c4f134e819619b3f09a"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/agl-service-cloudproxy;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "${AGL_APP_REVISION}"

PV      = "1.0+git${SRCPV}"
S       = "${WORKDIR}/git/"

DEPENDS = "azure-iot-sdk-c glib-2.0"

inherit cmake aglwgt pkgconfig

# Azure include files
CXXFLAGS_prepend += "-I${STAGING_INCDIR}/azureiot"

RDEPENDS_${PN} += "azure-iot-sdk-c azure-c-shared-utility"

BBCLASSEXTEND = "native nativesdk"

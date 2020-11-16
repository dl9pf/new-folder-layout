SUMMARY = "The software for AGL Cluster Demo Qtwayland Compositor"
DESCRIPTION = "A set of packages belong to AGL Cluster Demo Qtwayland Compositor"

LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-cluster-demo-qtcompositor \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    packagegroup-agl-profile-cluster-qtcompositor \
    packagegroup-agl-ttf-fonts \
    packagegroup-agl-networking \
    "

AGL_SERVICE = " \
    agl-service-bluetooth \
    agl-service-can-low-level \
    agl-service-data-persistence \
    agl-service-gps \
    agl-service-network \
    agl-service-unicens \
    agl-service-hvac \
    agl-service-nfc \
    agl-service-identity-agent \
    "

AGL_APPS = " \
    cluster-gauges-qtcompositor \
    "

RDEPENDS_${PN}_append = " \
    libva-utils \
    linux-firmware-ralink \
    can-utils \
    most \
    ${AGL_SERVICE} \
    ${AGL_APPS} \
"

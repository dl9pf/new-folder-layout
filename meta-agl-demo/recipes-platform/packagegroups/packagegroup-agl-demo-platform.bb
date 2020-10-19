SUMMARY = "The software for DEMO platform of AGL IVI profile"
DESCRIPTION = "A set of packages belong to AGL Demo Platform"

LICENSE = "MIT"

inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES = "\
    packagegroup-agl-demo-platform \
    packagegroup-agl-demo-platform-devel \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    packagegroup-agl-image-ivi \
    "

RDEPENDS_${PN} += "\
    packagegroup-agl-profile-graphical-qt5 \
    packagegroup-hmi-framework \
    packagegroup-agl-demo \
    "

AGL_APPS = " \
    dashboard \
    hvac \
    mediaplayer \
    virtual/navigation \
    phone \
    poiapp \
    radio \
    settings \
    messaging \
    ${@bb.utils.contains('DISTRO_FEATURES', 'agl-devel', 'taskmanager' , '', d)} \
    virtual/mixer \
    virtual/qtwayland-config \
    "

QTAGLEXTRAS = " libqtappfw"

# add support for websocket in Qt and QML
QTAGLEXTRAS_append = " qtwebsockets qtwebsockets-qmlplugins"
#PREFERRED_PROVIDER_virtual/webruntime = "web-runtime"

# Cluster demo support.
# ATM no cluster map viewer is supported with the older navigation application.
MAPVIEWER = "${@bb.utils.contains("PREFERRED_RPROVIDER_virtual/navigation", "ondemandnavi", "tbtnavi", "",d)}"
CLUSTER_SUPPORT_PACKAGES = " \
	${MAPVIEWER} \
	cluster-demo-network-config \
	cluster-lin-bridging-config \
	cluster-demo-simulator \
"
CLUSTER_SUPPORT = "${@bb.utils.contains("DISTRO_FEATURES", "agl-cluster-demo-support", "${CLUSTER_SUPPORT_PACKAGES}", "",d)}"

DEMO_UNIT_CONF ?= ""
# Hook for demo platform configuration
# ATM used for:
# 1) Adding udev configuration and scripts for supporting USB attached
#    I2C devices for RTC and HVAC LED support.
DEMO_UNIT_CONF += " demo-i2c-udev-conf "

# Preload poi API key for demo if requested, and potentially maps for older
# navigation application if it is configured.
DEMO_MAPS_LOCALE ?= "uk"
DEMO_PRELOAD_MAPS = "${@bb.utils.contains("PREFERRED_RPROVIDER_virtual/navigation", "navigation", " navigation-maps-${DEMO_MAPS_LOCALE}", "",d)}"

# Preload only if agl-demo-preload is set
DEMO_PRELOAD = "${@bb.utils.contains("DISTRO_FEATURES", "agl-demo-preload", " ${DEMO_PRELOAD_MAPS} ${DEMO_UNIT_CONF} poiapp-api-key", "",d)}"

RDEPENDS_${PN}_append = " \
    qtquickcontrols2-agl \
    qtquickcontrols2-agl-style \
    ${@bb.utils.contains('DISTRO_FEATURES', 'agl-devel', 'unzip' , '', d)} \
    ${AGL_APPS} \
    ${QTAGLEXTRAS} \
    ${CLUSTER_SUPPORT} \
    ${DEMO_PRELOAD} \
    "

# NOTE: Currently no coverage versions for the application widgets,
#       they should be added here when available.
#       Also, the navigation and mixer debug widgets are currently
#       specified explicitly, as there's no simple way to derive their
#       names from the virtual/ RPROVIDES at present.
RDEPENDS_${PN}-devel = " \
    packagegroup-hmi-framework-devel \
    dashboard-dbg \
    hvac-dbg \
    mediaplayer-dbg \
    mixer-dbg \
    phone-dbg \
    ondemandnavi-dbg \
    poiapp-dbg \
    radio-dbg \
    settings-dbg \
    messaging-dbg \
    taskmanager-dbg \
    "

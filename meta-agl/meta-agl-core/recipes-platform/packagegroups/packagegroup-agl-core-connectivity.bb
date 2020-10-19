DESCRIPTION = "The minimal set of packages for Connectivity Subsystem"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-core-connectivity \
    "

ALLOW_EMPTY_${PN} = "1"

PKGGROUP_ZEROCONF = "${@bb.utils.contains('DISTRO_FEATURES', 'zeroconf', 'packagegroup-base-zeroconf', '', d)}"

RDEPENDS_${PN} += "\
    dhcp-server \
    ${@bb.utils.contains('VIRTUAL-RUNTIME_net_manager','connman','connman connman-client connman-tests \
        connman-tools connman-ncurses connman-plugin-session-policy-local','',d)} \
    ${@bb.utils.contains('AGL_FEATURES', 'agl-devel', '${PKGGROUP_ZEROCONF}', '', d)} \
    "

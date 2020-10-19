FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-dt-dtoverlays-Add-jailhouse-memory-DT-overlay.patch"


LINUX_VERSION = "5.4.51"
SRCREV = "2c8ec3bb4403a7c76c22ec6d3d5fc4b2a366024e"

require recipes-kernel/linux/linux-jailhouse-5.4.inc

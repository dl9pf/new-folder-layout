FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += "${@bb.utils.contains("DISTRO_FEATURES", "weston-remoting", "file://remote-output.cfg", "",d)}"

# For virtual machines and intel-corei7-64 we want to support both the HDMI-A-1
# and Virtual-1 outputs. This allows us to run virtual images on real hardware
# and vice versa.
SRC_URI_append_qemuall = " file://virtual.cfg"
SRC_URI_append_intel-corei7-64 = " file://virtual.cfg"

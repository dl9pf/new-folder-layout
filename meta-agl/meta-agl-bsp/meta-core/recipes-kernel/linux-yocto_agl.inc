FILESEXTRAPATHS_prepend := "${THISDIR}/linux-yocto:"


require recipes-kernel/linux/linux-agl.inc

# Extra configuration options for the QEMU kernel
SRC_URI += "file://hciattach.cfg \
	    file://virtio.cfg \
            "

# Enable some things on qemuarm64 so MOST drivers will build and load.
SRC_URI_append_qemuarm64 = " file://most_deps.cfg"

# Configuration for using the virt machine (and not versatilepb)
SRC_URI_append_qemuarm = " file://qemuarm.cfg"

# Build a generic v7 kernel instead of the arm926j one that upstream
# qemuarm defaults to.
KBUILD_DEFCONFIG_qemuarm = "multi_v7_defconfig"
KCONFIG_MODE = "--alldefconfig"


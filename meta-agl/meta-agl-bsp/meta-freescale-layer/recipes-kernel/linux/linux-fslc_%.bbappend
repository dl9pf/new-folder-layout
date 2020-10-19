FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

require recipes-kernel/linux/linux-agl.inc

# Make sure these are enabled so that AGL configurations work
SRC_URI_append = " file://tmpfs.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/tmpfs.cfg"
SRC_URI_append = " file://namespace.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/namespace.cfg"
SRC_URI_append = " file://cgroup.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/cgroup.cfg"

# Support for CFG80211 subsystem
SRC_URI_append = " file://cfg80211.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/cfg80211.cfg"

# Turn off a couple of things enabled by default by Freescale
# (lock debugging and userspace firmware loader fallback)
SRC_URI_append = " file://fixups.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fixups.cfg"

do_install_append_cubox-i() {
    # Add symlink to work with default Hummingboard 2 u-boot configuration
    ln -sf imx6q-hummingboard2.dtb ${D}/boot/imx6q-hummingboard2-emmc.dtb
}

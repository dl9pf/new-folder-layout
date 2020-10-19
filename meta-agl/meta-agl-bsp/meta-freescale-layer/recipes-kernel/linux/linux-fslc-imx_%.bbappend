FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:${THISDIR}/files:"

require recipes-kernel/linux/linux-agl.inc

# These patches and the configuration fragment below will need to be
# revisited if/when using IMX_DEFAULT_BSP = "mainline" with i.MX8
# becomes more feasible with upstream meta-freescale.
SRC_URI_append_etnaviv = " \
    file://0001-enable-mhdp-with-etnaviv.patch \
    file://0002-dts-enable-etnaviv.patch \
    file://0003-drm-etnaviv-fix-TS-cache-flushing-on-GPUs-with-BLT-e.patch \
    file://0004-drm-sched-Fix-passing-zero-to-PTR_ERR-warning-v2.patch \
"

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

# Support for i.MX8MQ EVKB (e.g. Broadcom wifi)
SRC_URI_append_imx8mqevk = " file://imx8mq-evkb.cfg"
KERNEL_CONFIG_FRAGMENTS_append_imx8mqevk = " ${WORKDIR}/imx8mq-evkb.cfg"

# Build in etnaviv if required
SRC_URI_append_etnaviv = " file://etnaviv.cfg"
KERNEL_CONFIG_FRAGMENTS_append_etnaviv = " ${WORKDIR}/etnaviv.cfg"

# Turn off a couple of things enabled by default by Freescale
# (lock debugging and userspace firmware loader fallback)
SRC_URI_append = " file://fixups.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fixups.cfg"

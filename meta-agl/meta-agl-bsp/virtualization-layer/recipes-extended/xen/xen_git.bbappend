FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# make the package machine-specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

# rpi4 specifics
LIC_FILES_CHKSUM_raspberrypi4 = "file://COPYING;md5=4295d895d4b5ce9d070263d52f030e49"
XEN_REL_raspberrypi4 = "4.13"

SRCREV_raspberrypi4 = "721f2c323ca55c77857c93e7275b4a93a0e15e1f"
SRC_URI_append_raspberrypi4 = " file://0001-XEN-on-RPi4-1GB-lmitation-workaround-XEN-tries-to-al.patch"

#due to incorrect xen binary preparation in external library, we add additional deploy
do_deploy_append_raspberrypi4() {
    if [ -f ${B}/xen/xen ]; then
        install -m 0644 ${B}/xen/xen ${DEPLOYDIR}/xen-${MACHINE}
    fi
}

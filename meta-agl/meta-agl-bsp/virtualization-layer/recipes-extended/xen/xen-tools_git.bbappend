
# make the package specific to the machine for now
PACKAGE_ARCH = "${MACHINE_ARCH}"

# rpi4
LIC_FILES_CHKSUM_raspberrypi4 = "file://COPYING;md5=4295d895d4b5ce9d070263d52f030e49"
XEN_REL_raspberrypi4 = "4.13"
SRCREV_raspberrypi4 = "721f2c323ca55c77857c93e7275b4a93a0e15e1f"
SRC_URI_raspberrypi4 = " \
    git://xenbits.xen.org/xen.git;branch=${XEN_BRANCH} \
    "

DESCRIPTION = "V3X specific packages"

LICENSE = "BSD-3-Clause & GPLv2+ & LGPLv2+"

inherit packagegroup

PACKAGES = " \
    packagegroup-v3x \
    packagegroup-v3x-test \
"

# V3x common packages: IMP UIO, CMEM, CV lib, ICCOM
RDEPENDS_packagegroup-v3x = " \
    kernel-module-cmemdrv \
    kernel-module-iccom \
    kernel-module-uio-imp \
    kernel-module-uio-isp \
    libiccom \
    udev-rules-cvlib \
"

# V3x test and benchmark packages
RDEPENDS_packagegroup-v3x-test = " \
    br-test \
    dbench \
    dhrystone \
    ${@bb.utils.contains("LICENSE_FLAGS_WHITELIST", "non-commercial", "netperf", "", d)} \
    whetstone \
"

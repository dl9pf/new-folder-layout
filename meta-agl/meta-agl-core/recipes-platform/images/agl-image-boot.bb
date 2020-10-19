SUMMARY = "An AGL small image just capable of allowing a device to boot."

require agl-image-boot.inc

LICENSE = "MIT"

IMAGE_INSTALL_append = "\
    packagegroup-agl-image-boot \
    "

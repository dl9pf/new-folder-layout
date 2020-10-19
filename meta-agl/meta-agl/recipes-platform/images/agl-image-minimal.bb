SUMMARY = "An AGL small image just capable of allowing a device to boot."

require agl-image-minimal.inc

LICENSE = "MIT"

IMAGE_INSTALL_append = "\
    packagegroup-agl-image-minimal \
    "

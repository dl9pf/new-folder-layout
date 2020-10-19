SUMMARY = "An AGL small image just capable of allowing a device to boot and run tests."

DESCRIPTION = "A minimal set of AGL Distribution. This image also has additional \
packages (e.g. commandline tools) for Quality Assurance(QA)."

require agl-image-minimal.bb

LICENSE = "MIT"

IMAGE_INSTALL_append = " \
    packagegroup-ivi-common-test \
    "

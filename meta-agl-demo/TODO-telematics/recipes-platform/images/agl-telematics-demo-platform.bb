DESCRIPTION = "AGL Telematics Demo Platform image."

require agl-telematics-demo-platform.inc

LICENSE = "MIT"

IMAGE_FEATURES_append = " \
    "

IMAGE_INSTALL_append = " \
    packagegroup-agl-telematics-demo-platform \
    "


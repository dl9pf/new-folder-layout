DESCRIPTION = "AGL Demo Platform image currently contains a simple HMI and \
demos."

require agl-demo-platform.inc

LICENSE = "MIT"

# The demo will not work on the pi3 due to the gfx memory
# and the applications requiring FHD (SPEC-390)
COMPATIBLE_MACHINE_raspberrypi3 = "(^$)"

# Always include the test widgets
IMAGE_FEATURES_append = " agl-test-wgt"

# add packages for demo platform (include demo apps) here
IMAGE_INSTALL_append = " \
    packagegroup-agl-demo-platform \
    distro-build-manifest \
    "


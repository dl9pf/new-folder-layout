SUMMARY = "An image containing all packages required to run web applications"

require agl-image-graphical-html5.inc

LICENSE = "MIT"

IMAGE_INSTALL_append = "\
    packagegroup-agl-profile-graphical-html5 \
    "


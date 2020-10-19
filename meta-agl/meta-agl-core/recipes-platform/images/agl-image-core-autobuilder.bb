SUMMARY = "A very basic Wayland image with a terminal"

require agl-image-weston.inc

LICENSE = "MIT"

IMAGE_INSTALL_append = "\
    packagegroup-agl-profile-graphical \
    busybox \
    psplash \
    gdb \
    gperf \
    weston \
    weston-examples \
    curl \
    ptest-runner \
    libsoup-2.4 \
    "

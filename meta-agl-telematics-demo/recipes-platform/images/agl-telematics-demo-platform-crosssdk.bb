SUMMARY = "Cross SDK of AGL Distribution for telematics profile Demo"

DESCRIPTION = "SDK image for AGL Distribution for Telematics profile Demo. \
It includes the full meta-toolchain, plus developement headers and libraries \
to form a standalone cross SDK."

require agl-telematics-demo-platform.bb

LICENSE = "MIT"

IMAGE_FEATURES_append = " dev-pkgs"
IMAGE_INSTALL_append = " kernel-dev kernel-devsrc"

inherit populate_sdk

# Task do_populate_sdk and do_rootfs can't be exec simultaneously.
# Both exec "createrepo" on the same directory, and so one of them
# can failed (randomly).
addtask do_populate_sdk after do_rootfs

TOOLCHAIN_HOST_TASK_append = " nativesdk-perl-modules "

SUMMARY = "Cross SDK of Full AGL Distribution for IVI profile"

DESCRIPTION = "SDK image for full AGL Distribution for IVI profile. \
It includes the full meta-toolchain, plus developement headers and libraries \
to form a standalone cross SDK."

require agl-image-graphical-qt5.bb

LICENSE = "MIT"

require agl-image-graphical-qt5-crosssdk.inc

inherit populate_sdk populate_sdk_qt5

# Task do_populate_sdk and do_rootfs can't be exec simultaneously.
# Both exec "createrepo" on the same directory, and so one of them
# can failed (randomly).
addtask do_populate_sdk after do_rootfs

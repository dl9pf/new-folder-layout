FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PACKAGECONFIG ?= "jpeg png v4l libv4l gstreamer"

SRC_URI += " \
        file://0004-Change-Max-Device-Count-To-16.patch \
        "

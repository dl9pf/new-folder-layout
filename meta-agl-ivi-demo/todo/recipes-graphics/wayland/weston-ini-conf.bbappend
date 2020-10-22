FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_remove = " \
        file://hdmi-a-1-270.cfg \
        file://hdmi-a-1-90.cfg \
        file://virtual.cfg \
"

SRC_URI += " \
        file://hdmi-a-1-180.cfg \
        file://virtual-landscape.cfg \
"

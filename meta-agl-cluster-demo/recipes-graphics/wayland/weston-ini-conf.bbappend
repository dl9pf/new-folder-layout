# Remove all rotated portrait mode config fragments, and add our own
# instead, as the cluster demo display is landscape orientation.
# NOTES:
# (1) Cannot over-ride virtual.cfg simply by having a copy here and
#     getting it picked up via our FILESEXTRAPATHS_prepend due to AGL's
#     layer priority scheme (this layer is lower priority than
#     meta-agl-bsp), so need to remove it and add a replacement.
# (2) The HDMI-A-1 config here uses a transform of 180 degrees to work
#     with the monitor setup in the AGL demo hardware platform.  The
#     virtual-landscape.cfg is left unrotated, since it's used with
#     QEMU on the desktop.

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI_remove = " \
	file://hdmi-a-1-270.cfg \
	file://hdmi-a-1-90.cfg \
	file://virtual.cfg \
"
SRC_URI += " \
	file://hdmi-a-1-180.cfg \
	file://virtual-landscape.cfg \
"

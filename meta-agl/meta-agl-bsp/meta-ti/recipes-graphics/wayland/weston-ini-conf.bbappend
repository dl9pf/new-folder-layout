FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# While we could have a panel attached to HDMI, we only use the default
# panel isntead.
SRC_URI_remove_dra7xx-evm = "file://hdmi-a-1-270.cfg"
# Our DPI panel shows up as "UNNAMED-1"
SRC_URI_append_dra7xx-evm = " file://unnamed.cfg"

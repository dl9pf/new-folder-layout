FILESEXTRAPATHS_prepend := "${THISDIR}/pulseaudio:"

################### F I X M E ####################
# FIXME pyro
DISABLED_SRC_URI += " \
	file://dra7xx-evm-set-default-sink-source.patch \
"

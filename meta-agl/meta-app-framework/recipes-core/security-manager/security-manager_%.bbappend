FILESEXTRAPATHS_prepend := "${THISDIR}/security-manager:"

EXTRA_OECMAKE =+ " -DGLOBALUSER=afm"
SRC_URI += " \
   file://0001-Adapt-rules-to-AGL.patch \
"


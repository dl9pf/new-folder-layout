
BBLAYERS =+ " \
	${METADIR}/meta-agl/meta-pipewire \
	"

# we ship our own pipewire
BBMASK += "meta-openembedded/meta-oe/recipes-multimedia/pipewire/"

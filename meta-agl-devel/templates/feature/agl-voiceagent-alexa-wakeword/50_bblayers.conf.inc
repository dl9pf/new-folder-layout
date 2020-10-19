BBLAYERS =+ " \
	${METADIR}/external/alexa-auto-sdk/extensions/extras/amazonlite/meta-aac-amazonlite \
"

# Include the recipe for the required module as well
BBFILES += "${METADIR}/external/alexa-auto-sdk/extensions/extras/amazonlite/modules/*/*.bb"

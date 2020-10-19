DESCRIPTION = "The set of packages for AGL Speech Subsystem"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-speech-services \
    "

RDEPENDS_${PN} += "\
    agl-service-voice-high \
    agl-service-voice-high-capabilities \
    ${PREFERRED_RPROVIDER_virtual/voice-high-config} \
"

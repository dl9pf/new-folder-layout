SUMMARY = "Alexa service binding default configuration to connect to Alexa"
DESCRIPTION = "Alexa alexa-voiceagent-service binding configuration files"
HOMEPAGE = "https://github.com/alexa/alexa-auto-sdk/tree/master/platforms/agl/alexa-voiceagent-service"
SECTION = "apps"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "file://AlexaAutoCoreEngineConfig.json.in"

inherit allarch

ALEXA_WAKEWORD ??= "false"

do_compile () {

    if test x"${ALEXA_CLIENTID}" == x"" ; then
	bbfatal "ALEXA CLIENTID not defined in your environment e.g. conf/local.conf"
    fi

    if test x"${ALEXA_SERIALNUMBER}" == x"" ; then
	bbfatal "ALEXA SERIALNUMBER not defined in your environment e.g. conf/local.conf"
    fi

    if test x"${ALEXA_PRODUCTID}" == x"" ; then
	bbfatal "ALEXA PRODUCTID not defined in your environment e.g. conf/local.conf"
    fi

}

do_install () {
    #replace
    sed -e "s/@@ALEXA_CLIENTID@@/${ALEXA_CLIENTID}/" -e "s/@@ALEXA_SERIALNUMBER@@/${ALEXA_SERIALNUMBER}/" -e "s/@@ALEXA_PRODUCTID@@/${ALEXA_PRODUCTID}/" -e "s/@@WAKEWORD@@/${ALEXA_WAKEWORD}/" ${WORKDIR}/AlexaAutoCoreEngineConfig.json.in > ${WORKDIR}/AlexaAutoCoreEngineConfig.json

    # install
    install -D -m 644 ${WORKDIR}/AlexaAutoCoreEngineConfig.json ${D}${sysconfdir}/xdg/AGL/AlexaAutoCoreEngineConfig.json
}

RPROVIDES_${PN} += "virtual/alexa-voiceagent-config"


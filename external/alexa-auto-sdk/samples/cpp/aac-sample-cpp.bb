SUMMARY = "Alexa Auto SDK C++ Sample App"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://SampleApp/src/main.cpp;beginline=4;endline=13;md5=527e9938f0eaf4dbc8d3b17563870ae7"

DEPENDS = "aac-module-core aac-module-alexa aac-module-navigation aac-module-phone-control aac-module-cbl aac-module-address-book"

PACKAGECONFIG[alexacomms] = "-DALEXACOMMS=ON,,aac-module-communication"
PACKAGECONFIG[amazonlite] = "-DAMAZONLITE=ON,,aac-module-amazonlite"
PACKAGECONFIG[lvc] = "-DLOCALVOICECONTROL=ON,,aac-module-car-control aac-module-local-skill-service aac-module-local-voice-control aac-module-address-book-local-service"
PACKAGECONFIG[gstreamer] = "-DGSTREAMER=ON,,aac-module-gstreamer"
PACKAGECONFIG[dcm] = "-DDCM=ON,,aac-module-dcm-native-metrics"
PACKAGECONFIG[loopback-detector] = "-DLOOPBACK_DETECTOR=ON,,aac-module-loopback-detector"

AAC_ENABLE_ADDRESS_SANITIZER ?= "OFF"
EXTRA_OECMAKE += "-DAAC_ENABLE_ADDRESS_SANITIZER=${AAC_ENABLE_ADDRESS_SANITIZER} \
                  -DYOUR_CLIENT_ID=${MY_CLIENT_ID} \
                  -DYOUR_DEVICE_SERIAL_NUMBER=${MY_DEVICE_SERIAL_NUMBER} \
                  -DYOUR_PRODUCT_ID=${MY_PRODUCT_ID}"

inherit aac-module

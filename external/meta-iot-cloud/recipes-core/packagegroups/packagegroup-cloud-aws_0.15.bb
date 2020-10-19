DESCRIPTION = "Packages for Amazon Web Services."
LICENSE = "MIT"

inherit packagegroup

PR = "r0"

PACKAGES = "${PN}"

PACKAGECONFIG ??= "python cpp"

PACKAGECONFIG[python] = "\
    , \
    , \
    , \
    python3-aws-iot-device-sdk-python \
    python3-awscli \
"

PACKAGECONFIG[cpp] = "\
    , \
    , \
    , \
    aws-iot-device-sdk-cpp \
    aws-iot-device-sdk-cpp-dev \
"

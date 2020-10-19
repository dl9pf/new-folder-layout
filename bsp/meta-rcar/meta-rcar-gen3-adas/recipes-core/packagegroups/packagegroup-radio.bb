DESCRIPTION = "Radio packages"

LICENSE = "BSD-3-Clause & GPLv2+ & LGPLv2+"

inherit packagegroup

PACKAGES = " \
    packagegroup-radio \
"

RDEPENDS_packagegroup-radio = " \
    bluez5 \
    bluez5-testtools \
    linux-firmware-wl18xx \
    ofono \
    ofono-tests \
    pulseaudio-module-bluetooth-discover \
    pulseaudio-module-bluetooth-policy \
    pulseaudio-module-bluez5-device \
    pulseaudio-module-bluez5-discover \
    si-tools \
    ti-bt \
    ti-bt-firmware \
    iw \
"

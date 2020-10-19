IMAGE_INSTALL_append = " \
    packagegroup-bsp \
    packagegroup-bsp-custom \
    packagegroup-bsp-devdbg \
    packagegroup-bsp-utest \
    packagegroup-mm \
    packagegroup-radio \
    ${@bb.utils.contains('DISTRO_FEATURES', "opencv-sdk", "packagegroup-opencv-sdk", "", d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', "surroundview", "packagegroup-surroundview", "", d)} \
"

CONFLICT_DISTRO_FEATURES = "x11"

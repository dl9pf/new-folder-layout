IMAGE_INSTALL_append = " \
    packagegroup-bsp \
    packagegroup-bsp-custom \
    packagegroup-bsp-devdbg \
    packagegroup-bsp-utest \
    ${@bb.utils.contains('DISTRO_FEATURES', "opencv-sdk", "packagegroup-opencv-sdk", "", d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', "surroundview", "packagegroup-surroundview-drm", "", d)} \
"

IMAGE_INSTALL_append_rcar-gen3-v3x = " \
    packagegroup-bsp-python2 \
    packagegroup-v3x \
    packagegroup-v3x-test \
"

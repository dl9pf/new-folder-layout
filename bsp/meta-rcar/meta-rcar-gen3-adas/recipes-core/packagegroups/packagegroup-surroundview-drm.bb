DESCRIPTION = "Packages required for DRM Surroundview demo"

LICENSE = "GPLv2 & GPLv3 & BSD & MIT & GPLv2+ & LGPLv2+"

inherit packagegroup

PACKAGES = " \
    packagegroup-surroundview-drm \
"

RDEPENDS_packagegroup-surroundview-drm = " \
    gstreamer1.0-plugins-base libgstallocators-1.0 \
    gstreamer1.0-plugins-base-app libgstapp-1.0 \
    kernel-module-mmngr \
    kernel-module-mmngrbuf \
    libdrm \
    libspnav \
    mmngr-user-module \
    spacenavd \
"

DESCRIPTION = "BSP packages (explicitly requested by Renesas)"

LICENSE = "BSD-3-Clause & GPLv2+ & LGPLv2+"

inherit packagegroup

PACKAGES = " \
    packagegroup-bsp \
    packagegroup-bsp-custom \
    packagegroup-bsp-devdbg \
    packagegroup-bsp-utest \
"

PACKAGES_append_rcar-gen3-v3x = " \
    packagegroup-bsp-python2 \
"

# Packages mandatory for BSP (but often not needed)
RDEPENDS_packagegroup-bsp = " \
    cmake \
    g++ \
    gcc \
    git \
    git-perltools \
    make \
"

# Packages mandatory for BSP (useful for development/debug)
RDEPENDS_packagegroup-bsp-devdbg = " \
    atop \
    curl \
    devmem2 \
    gdb \
    gdbserver \
    iperf3 \
    iproute2 \
    iproute2-tc \
    linuxptp \
    nano \
    openssh \
    openssh-sftp \
    openssh-sftp-server \
    opkg \
    perf \
    procps \
    strace \
    vim \
    wget \
"

# Various packages needed for all boards
RDEPENDS_packagegroup-bsp-custom = " \
    bonnie++ \
    can-utils \
    capture \
    e2fsprogs \
    e2fsprogs-tune2fs \
    eglibc-utils \
    ethtool \
    iio-utils \
    kernel-devicetree \
    kernel-modules \
    ldd \
    libpcap \
    libsocketcan \
    lmbench \
    most-tools \
    mtd-utils \
    pciutils \
    rsync \
    spidev-dbg spidev-test \
    subversion \
    usbutils \
    util-linux \
    v4l2-fw \
    ${@bb.utils.contains("IMAGE_FEATURES", "ssh-server-openssh", "", "dropbear",d)} \
"

# Python2 packages requested by Renesas
RDEPENDS_packagegroup-bsp-python2 = " \
    python-dbus \
    python-nose \
    python-numpy \
    python-pygobject \
    python-pyyaml \
    python-setuptools \
"

# Utest (IMR, IMP, etc demos) related packages
RDEPENDS_packagegroup-bsp-utest = " \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-base-app \
    kernel-module-mmngr \
    kernel-module-mmngrbuf \
    libdrm \
    libgstallocators-1.0 \
    libgstapp-1.0 \
    libinput \
    libyaml \
    linux-renesas-uapi \
    mmngr-user-module \
    netevent \
    utest-cam-imr-drm \
"

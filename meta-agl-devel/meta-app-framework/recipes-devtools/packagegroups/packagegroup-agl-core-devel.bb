SUMMARY = "Provides a set of tools for development for AGL DISTRO"
LICENSE = "MIT"

inherit packagegroup

RDEPENDS_${PN} = "\
        strace \
        ldd \
        less \
        vim \
        lsof \
        gdb \
        valgrind \
        perf \
        htop \
        powertop \
        screen \
        usbutils \
        rsync \
        tree \
        pstree \
        procps \
        jq \
        libxslt-bin \
        agl-service-network-tools \
        gcc-sanitizers \
        pciutils \
        pyagl \
        gcov \
        gcov-symlinks \
        gcovr \
        gcovr-wrapper \
        "

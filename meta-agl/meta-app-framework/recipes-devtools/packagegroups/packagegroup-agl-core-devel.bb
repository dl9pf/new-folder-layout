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
        screen \
        usbutils \
        rsync \
        tree \
        pstree \
        procps \
        jq \
        libxslt-bin \
        gcc-sanitizers \
        pciutils \
        pyagl \
        gcov \
        gcov-symlinks \
        gcovr \
        gcovr-wrapper \
        "

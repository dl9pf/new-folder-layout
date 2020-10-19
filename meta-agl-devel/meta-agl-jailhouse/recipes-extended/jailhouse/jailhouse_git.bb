SUMMARY = "Linux-based partitioning hypervisor"
DESCRIPTION = "Jailhouse is a partitioning Hypervisor based on Linux. It is able to run bare-metal applications or (adapted) \
operating systems besides Linux. For this purpose, it configures CPU and device virtualization features of the hardware \
platform in a way that none of these domains, called 'cells' here, can interfere with each other in an unacceptable way."
HOMEPAGE = "https://github.com/siemens/jailhouse"
SECTION = "jailhouse"
LICENSE = "GPL-2.0 & BSD-2-Clause"

LIC_FILES_CHKSUM = " \
    file://COPYING;md5=9fa7f895f96bde2d47fd5b7d95b6ba4d \
"

SRCREV = "4ce7658dddfd5a1682a379d5ac46657e93fe1ff0"
PV = "0.12+git${SRCPV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/siemens/jailhouse \
           file://0001-configs-arm64-Add-support-for-RPi4-with-more-than-1G.patch \
           "

SRC_URI += "file://qemu-agl.c \
	    file://agl-apic-demo.c \
	    file://agl-pci-demo.c \
	    file://agl-ivshmem-demo.c \
	    file://agl-linux-x86-demo.c \
	    "

DEPENDS = "virtual/kernel dtc-native python3-mako-native make-native"

require jailhouse-arch.inc
inherit module python3native bash-completion setuptools3

S = "${WORKDIR}/git"
B = "${S}"

JH_DATADIR ?= "${datadir}/jailhouse"
JH_EXEC_DIR ?= "${libexecdir}/jailhouse"
CELL_DIR ?= "${JH_DATADIR}/cells"
INMATES_DIR ?= "${JH_DATADIR}/inmates"
DTS_DIR ?= "${JH_DATADIR}/cells/dts"

JH_CELL_FILES ?= "*.cell"

EXTRA_OEMAKE = "ARCH=${JH_ARCH} CROSS_COMPILE=${TARGET_PREFIX} CC="${CC}" KDIR=${STAGING_KERNEL_BUILDDIR}"

do_configure() {
		
	# copy ${WORKDIR}/qemu-agl.c ${S}/configs/x86/ <--- folder where the cells are defined in the source tree to be compiled
	cp ${WORKDIR}/qemu-agl.c ${S}/configs/${JH_ARCH}
	cp ${WORKDIR}/agl-apic-demo.c ${S}/configs/x86/
	cp ${WORKDIR}/agl-pci-demo.c ${S}/configs/x86/
	cp ${WORKDIR}/agl-linux-x86-demo.c ${S}/configs/x86/
	cp ${WORKDIR}/agl-ivshmem-demo.c ${S}/configs/x86/

	sed -i '1s|^#!/usr/bin/env python$|#!/usr/bin/env python3|' ${B}/tools/${BPN}-*
}

do_compile() {
	oe_runmake
}

do_install() {
	# Install pyjailhouse python modules needed by the tools
	distutils3_do_install

	# We want to install the python tools, but we do not want to use pip...
	# At least with v0.10, we can work around this with
	# 'PIP=":" PYTHON_PIP_USEABLE=yes'
	oe_runmake PIP=: PYTHON=python3 PYTHON_PIP_USEABLE=yes DESTDIR=${D} install

	install -d ${D}${CELL_DIR}

	
	install -m 0644 ${B}/configs/${JH_ARCH}/${JH_CELL_FILES} ${D}${CELL_DIR}/

	install -d ${D}${INMATES_DIR}
	install -m 0644 ${B}/inmates/demos/${JH_ARCH}/*.bin ${D}${INMATES_DIR}

	if [ ${JH_ARCH}  != "x86" ]; then
		install -d ${D}${DTS_DIR}
		install -m 0644 ${B}/configs/${JH_ARCH}/dts/*.dtb ${D}${DTS_DIR}
	fi
}

PACKAGE_BEFORE_PN = "kernel-module-jailhouse pyjailhouse ${PN}-tools ${PN}-demos"
FILES_${PN} = "${base_libdir}/firmware ${libexecdir} ${sbindir} ${JH_DATADIR}"
FILES_pyjailhouse = "${PYTHON_SITEPACKAGES_DIR}"
FILES_${PN}-tools = "${libexecdir}/${BPN}/${BPN}-* ${JH_DATADIR}/*.tmpl"
FILES_${PN}-demos = "${JH_DATADIR}/ ${sbindir}/ivshmem-demo"

RDEPENDS_${PN}-tools = "pyjailhouse python3-mmap python3-math python3-datetime python3-curses python3-compression python3-mako"
RDEPENDS_pyjailhouse = "python3-core python3-ctypes python3-fcntl"
RDEPENDS_${PN}-demos = "jailhouse"

RRECOMMENDS_${PN} = "${PN}-tools"

KERNEL_MODULE_AUTOLOAD += "jailhouse"

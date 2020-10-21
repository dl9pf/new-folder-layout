DESCRIPTION = "Waltham is a network IPC library designed to resemble Wayland both protocol and protocol-API wise"
HOMEPAGE = "https://github.com/waltham/waltham"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://waltham-transmitter/COPYING;md5=f21c9af4de068fb53b83f0b37d262ec3"

DEPENDS += "libdrm virtual/kernel wayland wayland-native waltham weston gstreamer1.0 gstreamer gstreamer1.0-plugins-base gstreamer1.0-plugins-good gstreamer1.0-plugins-bad wayland-ivi-extension"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/weston-ivi-plugins.git;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "5287483228fa1e28f3217a54606cfe760c6582bd"

S = "${WORKDIR}/git/"

WALTHAM_PIPELINE_TRANSMITTER ?= "waltham-transmitter/waltham-renderer/pipeline_example_general.cfg"
WALTHAM_PIPELINE_RECEIVER ?= "waltham-receiver/receiver_pipeline_example_general.cfg"
WALTHAM_RECIEVER_IP ?= "192.168.1.2"
WALTHAM_RECEIVER_PORT ?= "3440"

inherit pkgconfig cmake

do_install_append () {
   install -d ${D}/etc/xdg/weston/
   install ${S}/${WALTHAM_PIPELINE_TRANSMITTER} ${D}/etc/xdg/weston/transmitter_pipeline.cfg
   install ${S}/${WALTHAM_PIPELINE_RECEIVER} ${D}/etc/xdg/weston/receiver_pipeline.cfg

   sed -i -e "s/YOUR_RECIEVER_IP/${WALTHAM_RECIEVER_IP}/g" ${D}/etc/xdg/weston/transmitter_pipeline.cfg
   sed -i -e "s/YOUR_RECIEVER_PORT/${WALTHAM_RECEIVER_PORT}/g" ${D}/etc/xdg/weston/transmitter_pipeline.cfg
   sed -i -e "s/YOUR_RECIEVER_PORT/${WALTHAM_RECEIVER_PORT}/g" ${D}/etc/xdg/weston/receiver_pipeline.cfg

}

FILES_${PN} += "/etc/xdg/weston/*.cfg"
FILES_${PN} += "${libdir}/*"
FILES_${PN} += "${bindir}/*"

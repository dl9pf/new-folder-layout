SUMMARY = "Cluster demo connman configuration"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://cluster.config"

CLUSTER_DEMO_SERVER_IP ?= "192.168.20.99"

do_install() {
    install -d ${D}${localstatedir}/lib/connman
    sed "s/@CLUSTER_DEMO_SERVER_IP@/${CLUSTER_DEMO_SERVER_IP}/g" \
        ${WORKDIR}/cluster.config > ${D}${localstatedir}/lib/connman/cluster.config
}

FILES_${PN} += "${localstatedir}/*"

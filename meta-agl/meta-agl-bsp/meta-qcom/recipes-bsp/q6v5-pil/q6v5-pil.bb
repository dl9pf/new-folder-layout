inherit systemd

SUMMARY = "Systemd unit file for the delay loading Hexagon PIL kernel module"
SECTION = "misc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI += "file://qcom-q6v5-pil.service"

do_install() {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/qcom-q6v5-pil.service ${D}${systemd_unitdir}/system

    # Blacklist qcom_q6v5_pil to prevent modules autoload
    # qcom-q6v5-pil.service will do the work after rmtfs done.
    install -d ${D}/${sysconfdir}/modprobe.d
    echo "blacklist qcom_q6v5_pil" > ${D}/${sysconfdir}/modprobe.d/qcom_q6v5_pil.conf
}

SYSTEMD_SERVICE_${PN} = "qcom-q6v5-pil.service"

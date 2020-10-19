SUMMARY = "Systemd synchronization script"
DESCRIPTION = "\
Systemd synchronization script \
reload daemon at the first boot. \
"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

#This script should be the last to be execute at the first boot
POST_INSTALL_LEVEL = "X0"
POST_INSTALL_SCRIPT ?= "${POST_INSTALL_LEVEL}-${PN}.sh"

do_install() {
    install -d ${D}/${sysconfdir}/agl-postinsts
    cat > ${D}/${sysconfdir}/agl-postinsts/${POST_INSTALL_SCRIPT} <<EOF
#!/bin/sh -e
echo "restart daemon ..."
result=0
systemctl daemon-reload
if [ \$? -ne 0 ]; then
    result=1
fi
systemctl restart sockets.target
if [ \$? -ne 0 ]; then
    result=1
fi

if [ \$result -eq 0 ]; then
    echo "restart daemon OK"
    exit \$result
else
    echo "restart daemon failed"
    exit \$result
fi
EOF
    chmod a+x ${D}/${sysconfdir}/agl-postinsts/${POST_INSTALL_SCRIPT}
}

FILES_${PN} = "${sysconfdir}/agl-postinsts/${POST_INSTALL_SCRIPT}"

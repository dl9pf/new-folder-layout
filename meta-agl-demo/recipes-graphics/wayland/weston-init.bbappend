FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

inherit agl-graphical

AGL_DEFAULT_WESTONSTART ??= "/usr/bin/agl-compositor --config ${sysconfdir}/xdg/weston/weston.ini"

WESTONSTART ??= "${AGL_DEFAULT_WESTONSTART} ${WESTONARGS}"
WESTONSTART_append = " ${@bb.utils.contains("IMAGE_FEATURES", "debug-tweaks", " --log=${DISPLAY_XDG_RUNTIME_DIR}/compositor.log", "",d)}"

WIFILES = " \
    file://weston.conf.in \
    file://tmpfiles.conf.in \
    file://zz-dri.rules.in \
    file://zz-input.rules.in \
    file://zz-tty.rules.in \
"

WIFILES_append_imx = " \
    file://zz-dri-imx.rules.in \
"

SRC_URI_append = " ${WIFILES}"

do_install_append() {
    # Remove upstream weston.ini to avoid conflict with weston-ini-conf package
    rm -f ${D}${sysconfdir}/xdg/weston/weston.ini

    # Remove upstream weston udev rules just to be safe
    rm -f ${D}${sysconfdir}/udev/rules.d/71-weston-drm.rules

    # Process ".in" files
    files=$(echo ${WIFILES} | sed s,file://,,g)
    for f in ${files}; do
        g=${f%.in}
        if [ "${f}" != "${g}" ]; then
            sed -e "s,@WESTONUSER@,${WESTONUSER},g" \
                -e "s,@WESTONGROUP@,${WESTONGROUP},g" \
                -e "s,@XDG_RUNTIME_DIR@,${DISPLAY_XDG_RUNTIME_DIR},g" \
                -e "s,@WESTONSTART@,${WESTONSTART},g" \
                    ${WORKDIR}/${f} > ${WORKDIR}/${g}
        fi
    done

    # Install weston drop-in
    install -d ${D}${systemd_system_unitdir}/weston@.service.d
    install -m644 ${WORKDIR}/weston.conf ${D}/${systemd_system_unitdir}/weston@.service.d/weston-init.conf

    # Install tmpfiles drop-in
    install -d ${D}${libdir}/tmpfiles.d
    install -m644 ${WORKDIR}/tmpfiles.conf ${D}${libdir}/tmpfiles.d/weston-init.conf

    # Install udev rules
    install -d ${D}${sysconfdir}/udev/rules.d
    for f in ${files}; do
        g=${f%.in}
        h=${g%.rules}
        if [ "${g}" != "${h}" ]; then
            install -m644 ${WORKDIR}/${g} ${D}${sysconfdir}/udev/rules.d
        fi
    done
}

FILES_${PN} += " \
    ${libdir}/tmpfiles.d/ \
    ${systemd_system_unitdir}/weston@.service.d/ \
"

SYSTEMD_AUTO_ENABLE = "enable"


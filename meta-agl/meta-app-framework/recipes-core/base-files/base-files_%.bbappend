RDEPENDS_${PN}_append_with-lsm-smack = " smack"
PACKAGE_WRITE_DEPS_append_with-lsm-smack = " smack-native"

do_install_append() {
    install -m 0700 -d ${D}/${sysconfdir}/skel
    chmod -R 0700 ${D}/${sysconfdir}/skel
    install -m 0700 -d ${D}/${sysconfdir}/skel/app-data
    install -m 0700 -d ${D}/${sysconfdir}/skel/.config
    install -m 0755 -d ${D}/var
    if [ -d ${D}/usr/local ]; then
        mv ${D}/usr/local ${D}/var
    else
        install -m 0755 -d ${D}/var/local
    fi
    ln -s ../var/local ${D}/usr/local
}

do_install_append_with-lsm-smack () {
    install -d ${D}/${sysconfdir}/smack/accesses.d
    cat > ${D}/${sysconfdir}/smack/accesses.d/default-access-domains-no-user <<EOF
System User::App-Shared rwxat
System User::Home       rwxat
EOF
    chmod 0644 ${D}/${sysconfdir}/smack/accesses.d/default-access-domains-no-user
}

pkg_postinst_${PN}_append_with-lsm-smack() {
    chsmack -r -a 'User::Home' -t -D $D/${sysconfdir}/skel
    chsmack -a 'User::App-Shared' -D $D/${sysconfdir}/skel/app-data
    cp -rTf --preserve=all $D/${sysconfdir}/skel $D/${ROOT_HOME}
}


require af-binder_${PV}.inc

DEPENDS = "file json-c libmicrohttpd systemd util-linux openssl cynara"

inherit cmake pkgconfig

EXTRA_OECMAKE_append_class-target = "\
	-DUNITDIR_SYSTEM=${systemd_system_unitdir} \
"

EXTRA_OECMAKE_append_agl-devel = " \
	-DAGL_DEVEL=ON \
	-DINCLUDE_MONITORING=ON \
	-DINCLUDE_SUPERVISOR=ON -DAFS_SUPERVISION_SOCKET=/run/platform/supervisor \
"

pkg_postinst_${PN}() {
	mkdir -p "$D${libdir}/afb"
}

do_install_append_agl-devel_class-target() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d -m 0755 ${D}${systemd_system_unitdir}/multi-user.target.wants
        ln -s ../afm-api-supervisor.service ${D}${systemd_system_unitdir}/multi-user.target.wants/afm-api-supervisor.service
    fi
}

#############################################
# main package
#############################################

FILES_${PN}_append_agl-devel = " ${libdir}/afb/monitoring ${systemd_system_unitdir}"

RDEPENDS_${PN}-dev += "libafbwsc-dev"

#############################################
# intrinsic binding packages
#############################################
PACKAGES =+ "${PN}-intrinsic-bindings"
ALLOW_EMPTY_${PN}-intrinsic-bindings = "1"

PACKAGES_DYNAMIC = "${PN}-binding-*"

python populate_packages_prepend () {
    afb_libdir = d.expand('${libdir}/afb')
    postinst = d.getVar('binding_postinst', True)
    pkgs = []

    pkgs += do_split_packages(d, afb_libdir, '(.*)-api\.so$', d.expand('${PN}-binding-%s'), 'AFB binding for %s', postinst=postinst, extra_depends=d.expand('${PN}'))
    pkgs += do_split_packages(d, afb_libdir, '(.*(?!-api))\.so$', d.expand('${PN}-binding-%s'), 'AFB binding for %s', postinst=postinst, extra_depends=d.expand('${PN}'))

    d.setVar('RDEPENDS_' + d.getVar('PN', True) + '-intrinsic-bindings', ' '.join(pkgs))
}

#############################################
# tool package
#############################################
PACKAGES =+ "${PN}-tools"

FILES_${PN}-tools = "\
	${bindir}/afb-client-demo \
"

#############################################
# setup libafbwsc package
#############################################
PACKAGES =+ "libafbwsc libafbwsc-dev"

FILES_libafbwsc = "\
	${libdir}/libafbwsc.so.* \
"
FILES_libafbwsc-dev = "\
	${includedir}/afb/afb-wsj1.h \
	${includedir}/afb/afb-ws-client.h \
	${libdir}/libafbwsc.so \
	${libdir}/pkgconfig/libafbwsc.pc \
"

#############################################
# devtool package
#############################################
PACKAGES =+ "${PN}-devtools"

FILES_${PN}-devtools = "\
	${bindir}/afb-exprefs \
	${bindir}/afb-json2c \
	${bindir}/afb-genskel \
"

#############################################
# supervisor package
#############################################
PACKAGES_append_agl-devel = " ${PN}-supervisor "

FILES_${PN}-supervisor_agl-devel = "\
	${bindir}/afs-supervisor \
        ${systemd_system_unitdir} \
"

#############################################
# setup sample packages
#############################################
PACKAGES =+ "${PN}-samples"

FILES_${PN}-samples = "\
	${datadir}/af-binder \
"

#############################################
# meta package
#############################################
PACKAGES =+ "${PN}-meta"
ALLOW_EMPTY_${PN}-meta = "1"

RDEPENDS_${PN}-meta += "${PN} ${PN}-tools libafbwsc ${PN}-intrinsic-bindings"
RDEPENDS_${PN}-meta_append_agl-devel = " ${PN}-supervisor "


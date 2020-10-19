require af-main_${PV}.inc 

# NOTE: using libcap-native and setcap in install doesn't work
# NOTE: maybe setting afm_name to agl-framework is cleaner but has implications
# NOTE: there is a hack of security for using groups and dbus (to be checked)
# NOTE: using ZIP programs creates directories with mode 777 (very bad)

inherit cmake pkgconfig useradd systemd
BBCLASSEXTEND = "native"

SECTION = "base"

DEPENDS = "openssl libxml2 xmlsec1 systemd libzip json-c systemd security-manager af-binder sed m4"
DEPENDS_class-native = "openssl libxml2 xmlsec1 libzip json-c"
RDEPENDS_${PN}_class-target += "af-binder-tools nss-localuser cynagoauth"

PACKAGE_WRITE_DEPS_append_with-lsm-smack = " smack-native libcap-native"

EXTRA_OECMAKE_append_class-native  = "\
	-DUSE_LIBZIP=1 \
	-DUSE_SIMULATION=1 \
	-DUSE_SDK=1 \
	-DAGLVERSION=${AGLVERSION} \
	-Dafm_name=${afm_name} \
	-Dafm_confdir=${afm_confdir} \
	-Dafm_datadir=${afm_datadir} \
"

EXTRA_OECMAKE_append_class-target = "\
	-DUSE_LIBZIP=1 \
	-DUSE_SIMULATION=0 \
	-DUSE_SDK=0 \
	-DAGLVERSION=${AGLVERSION} \
	-Dafm_name=${afm_name} \
	-Dafm_confdir=${afm_confdir} \
	-Dafm_datadir=${afm_datadir} \
	-Dsystemd_units_root=${systemd_units_root} \
	-DUNITDIR_USER=${systemd_user_unitdir} \
	-DUNITDIR_SYSTEM=${systemd_system_unitdir} \
"

# ------------------------ WARNING WARNING WARNNING ---------------------------
#
# ATM (FF.rc2), forcing all apps to be signed is an issue when building without
# agl-devel feature. A workaround is to define ALLOW_NO_SIGNATURE=ON for all
# builds but this must be removed later. See SPEC-1614 for more details.
#
# A variable AGL_FORBID_UNSIGNED_APPS is introduced to enable/disable this 
# workaround in local.conf and allow transition to signed apps:
# * forbid unsigned apps by setting: AGL_FORBID_UNSIGNED_APPS="1"
# * [DEFAULT] allow unsigned apps: do nothing (or set: AGL_FORBID_UNSIGNED_APPS="0")
AGL_FORBID_UNSIGNED_APPS ?= "0"
#
# WORKAROUND:
EXTRA_OECMAKE_append_agl-devel = " -DAGL_DEVEL=1"
EXTRA_OECMAKE_append = " ${@bb.utils.contains('AGL_FORBID_UNSIGNED_APPS','1','','-DALLOW_NO_SIGNATURE=ON', d)}"
#
# Correct version (IMPORTANT TODO: to be restored later):
#EXTRA_OECMAKE_append_agl-devel = " -DAGL_DEVEL=1 -DALLOW_NO_SIGNATURE=ON"
#
# ------------------------ WARNING WARNING WARNNING ---------------------------


USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --gid ${afm_name} --home-dir ${afm_datadir} ${afm_name}"
GROUPADD_PARAM_${PN} = "--system ${afm_name}"

RDEPENDS_${PN}_append_with-lsm-smack = " smack bash"
DEPENDS_append_with-lsm-smack = " smack-native"

do_install_append_class-target() {
    install -d ${D}${bindir}
    install -d -m 0775 ${D}${systemd_units_root}/system
    install -d -m 0775 "${D}${systemd_units_root}/system/multi-user.target.wants"
    install -d -m 0775 "${D}${systemd_units_root}/system/afm-user-session@.target.wants"
    install -d -m 0775 ${D}${systemd_units_root}/user
    install -d -m 0775 ${D}${systemd_units_root}/user/default.target.wants
    install -d -m 0775 ${D}${systemd_units_root}/user/sockets.target.wants
    install -d ${D}${afm_datadir}/applications
    install -d ${D}${afm_datadir}/icons
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d -m 0755 ${D}${systemd_system_unitdir}/multi-user.target.wants
        install -d -m 0755 ${D}${systemd_system_unitdir}/sockets.target.wants
        ln -sf ../afm-system-setup.service ${D}${systemd_system_unitdir}/multi-user.target.wants/afm-system-setup.service
        ln -sf ../afm-system-daemon.service ${D}${systemd_system_unitdir}/multi-user.target.wants/afm-system-daemon.service
        ln -sf ../afm-system-daemon.socket ${D}${systemd_system_unitdir}/sockets.target.wants/afm-system-daemon.socket
    fi
}

pkg_postinst_ontarget_${PN}() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        chgrp ${afm_name} $D${systemd_units_root}/system
        chgrp ${afm_name} $D${systemd_units_root}/system/afm-user-session@.target.wants
        chgrp ${afm_name} $D${systemd_units_root}/user/default.target.wants
        chgrp ${afm_name} $D${systemd_units_root}/user/sockets.target.wants
    fi
    chown ${afm_name}:${afm_name} $D${afm_datadir}
    chown ${afm_name}:${afm_name} $D${afm_datadir}/applications
    chown ${afm_name}:${afm_name} $D${afm_datadir}/icons
}

pkg_postinst_ontarget_${PN}_append_with-lsm-smack() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/system
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/system/afm-user-session@.target.wants
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/user/default.target.wants
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/user/sockets.target.wants
    fi
    chsmack -a 'System::Shared' -t $D${afm_datadir}
    chsmack -a 'System::Shared' -t $D${afm_datadir}/applications
    chsmack -a 'System::Shared' -t $D${afm_datadir}/icons
}
FILES_${PN} += "${systemd_units_root}/* ${systemd_system_unitdir} ${systemd_user_unitdir}"
FILES_${PN}_append_agl-sign-wgts = " ${datadir}/afm"

PACKAGES =+ "${PN}-binding ${PN}-binding-dbg"
FILES_${PN}-binding = " ${afb_binding_dir}/afm-main-binding.so "
FILES_${PN}-binding-dbg = " ${afb_binding_dir}/.debug/afm-main-binding.so "

PACKAGES =+ "${PN}-tools ${PN}-tools-dbg"
FILES_${PN}-tools = "${bindir}/wgtpkg-*"
FILES_${PN}-tools-dbg = "${bindir}/.debug/wgtpkg-*"

SUMMARY = "AGL Framework Main part"
DESCRIPTION = "\
This is a core framework component for managing \
applications, widgets, and components. \
"

HOMEPAGE = "https://gerrit.automotivelinux.org/gerrit/#/admin/projects/src/app-framework-main"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/app-framework-main;protocol=https;branch=${AGL_BRANCH}"

SRCREV = "af8db35cc01c57b7d1d2e4fa7815df8c16f8454f"
PV = "${AGL_BRANCH}+git${SRCPV}"

S = "${WORKDIR}/git"

afm_name    = "afm"
afm_confdir = "${sysconfdir}/${afm_name}"
afm_datadir = "/var/local/lib/${afm_name}"
afb_binding_dir = "${libdir}/afb"
systemd_units_root = "/var/local/lib/systemd"

CFLAGS_append_agl-devel = " -DAGL_DEVEL"
# only install sample keys in agl-devel mode
# for production you need to deploy real keys
EXTRA_OECMAKE_append_agl-sign-wgts = " ${@bb.utils.contains('DISTRO_FEATURES', 'agl-devel', '-DINSTALL_SAMPLE_KEYS=ON', '-DINSTALL_SAMPLE_KEYS=OFF', d)}"




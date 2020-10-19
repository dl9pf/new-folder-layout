LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

require diffutils.inc

PR = "r7.0"

SRC_URI = "${GNU_MIRROR}/diffutils/diffutils-${PV}.tar.gz \
           file://diffutils_fix_for_automake-1.12.patch \
           file://fix_gcc6.patch \
           file://0001-Make-it-build-with-compile-time-hardening-enabled.patch \
           file://0002-included-libc-use-mempcpy-instead-of.patch \
           file://0003-context-fix-compilation-with-64bit-time_t-on-32bit-a.patch \
           "

SRC_URI[md5sum] = "71f9c5ae19b60608f6c7f162da86a428"
SRC_URI[sha256sum] = "c5001748b069224dd98bf1bb9ee877321c7de8b332c8aad5af3e2a7372d23f5a"

CACHED_CONFIGUREVARS = "\
    jm_cv_func_working_malloc=yes \
    jm_cv_func_working_realloc=yes \
"

do_configure_prepend () {
	chmod u+w ${S}/po/Makefile.in.in
}

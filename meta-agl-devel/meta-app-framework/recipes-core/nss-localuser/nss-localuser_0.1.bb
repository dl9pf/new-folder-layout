SUMMARY = "Name Service Switch module for resolving the local user hostname"

DESCRIPTION = "plugin for the GNU Name Service Switch (NSS) \
functionality of the GNU C Library (`glibc`) providing host name \
resolution for *"localuser"* family of virtual hostnames."

HOMEPAGE = "https://git.automotivelinux.org/src/nss-localuser/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=79ad77111c398994735201536a4749ba"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/src/nss-localuser;protocol=https;branch=${AGL_BRANCH}"
SRCREV = "66803c6fdb609ed83a78b9194ecb23e9c1b773e7"
PV = "${AGL_BRANCH}+git${SRCPV}"

RDEPENDS_${PN} = "base-files"

S = "${WORKDIR}/git"

do_compile() {
	make
}

do_install() {
	make nssdir=${D}${libdir} install
}

pkg_postinst_ontarget_${PN} () {
	sed -e '/^hosts:/s/\<localuser\>\s*//' \
		-e 's/\(^hosts:\s\s*\)\(.*\)/\1localuser \2/' \
		-i $D${sysconfdir}/nsswitch.conf
}

pkg_prerm_${PN} () {
	sed -e '/^hosts:/s/\<localuser\>\s*//' \
		-i $D${sysconfdir}/nsswitch.conf
}

INSANE_SKIP_${PN} = "ldflags"

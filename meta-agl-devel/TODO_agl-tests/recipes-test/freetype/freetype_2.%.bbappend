FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI =+ "${SOURCEFORGE_MIRROR}/freetype/freetype-demos/${PV}/ft2demos-${PV}.tar.gz;name=ft2demos \
           file://0001-Makefile-dont-build-gfx-demos.patch;patchdir=../ft2demos-${PV} \
           file://0001-ft2demos-Makefile-Do-not-hardcode-libtool-path.patch;patchdir=../ft2demos-${PV} \
          "
SRC_URI[ft2demos.md5sum] = "c376adf4782bac9b9ac8e427884752d2"
SRC_URI[ft2demos.sha256sum] = "5e9e94a2db9d1a945293a1644a502f6664a2173a454d4a55b19695e2e2f4a0bc"

PACKAGES =+ "${PN}-demos"

B = "${S}"

do_compile_append () {
    oe_runmake -C ${WORKDIR}/ft2demos-${PV} TOP_DIR=${WORKDIR}/${BPN}-${PV}/
}

do_install_append () {
    install -d -m 0755 ${D}/${bindir}
    for x in ftbench ftdump ftlint ftvalid ttdebug; do
        install -m 0755 ${WORKDIR}/ft2demos-${PV}/bin/.libs/$x ${D}/${bindir}
    done
}

FILES_${PN}-demos = "\
    ${bindir}/ftbench \
    ${bindir}/ftdump \
    ${bindir}/ftlint \
    ${bindir}/ftvalid \
    ${bindir}/ttdebug \
"

# enable pixmap/libpng support to allow color emojis
PACKAGECONFIG_append = " pixmap"

#
# aglwgt bbclass
#
# Jan-Simon Moeller, jsmoeller@linuxfoundation.org
#
# This class expects a "make package" target in the makefile
# which creates the wgt files in the package/ subfolder.
# The makefile needs to use wgtpkg-pack.
#

# 'wgtpkg-pack' in af-main-native is required.
DEPENDS_append = " af-main-native"

# for bindings af-binder is required.
DEPENDS_append = " af-binder"

# for bindings that use the cmake-apps-module
DEPENDS_append = " cmake-apps-module-native"

# for hal bindings genskel is required.
DEPENDS_append = " af-binder-devtools-native"

# Re-enable strip for qmake based projects (default value is "echo")
OE_QMAKE_STRIP = "${STRIP}"

# Extra build arguments passed to the autobuild script invocations
AGLWGT_EXTRA_BUILD_ARGS ?= 'VERBOSE=TRUE BUILD_ARGS="${PARALLEL_MAKE}"'

# CMake based widgets that inherit cmake.bbclass will have the
# following automatically appended to AGLWGT_EXTRA_BUILD_ARGS as
# the value of CONFIGURE_FLAGS.  This definition may need to be
# extended to include more of what is passed in cmake.bbclass's
# do_configure if it is found insufficient.  Using the generated
# toolchain.cmake file does fix issues with respect to finding the
# Qt5 CMake modules that seem difficult to fix otherwise, so at the
# very minimum it should be present.
AGLWGT_CMAKE_CONFIGURE_ARGS ?= "-DCMAKE_TOOLCHAIN_FILE=${WORKDIR}/toolchain.cmake ${EXTRA_OECMAKE}"

# Only widgets with recipe names starting with agl-service- are
# assumed to have tests by default, set this to "1" to force
# building/packaging of the test widget for other widgets.
AGLWGT_HAVE_TESTS ?= "0"

# Whether the widget should be auto-installed on first boot
AGLWGT_AUTOINSTALL ?= "1"

# Signature keys
# These are default keys for development purposes !
# Change it for production.
WGTPKG_AUTOSIGN_0_agl-sign-wgts ??= "${WORKDIR}/recipe-sysroot-native/usr/share/afm/keys/developer.key.pem:${WORKDIR}/recipe-sysroot-native/usr/share/afm/certs/developer.cert.pem"
WGTPKG_AUTOSIGN_1_agl-sign-wgts ??= "${WORKDIR}/recipe-sysroot-native/usr/share/afm/keys/platform.key.pem:${WORKDIR}/recipe-sysroot-native/usr/share/afm/certs/platform.cert.pem"

export WGTPKG_AUTOSIGN_0
export WGTPKG_AUTOSIGN_1

python __anonymous () {
    # NOTE: AGLWGT_CMAKE_CONFIGURE_ARGS is not updated directly here,
    #       but via the prefunc below to avoid issues around anonymous
    #       python ordering conflicts with e.g. externalsrc.bbclass.
    if bb.data.inherits_class("cmake", d):
        d.appendVarFlag('do_compile', 'prefuncs', ' aglwgt_cmake_configure')
}

python aglwgt_cmake_configure () {
    # Define CONFIGURE_FLAGS appropriately if cmake.bbclass has been
    # inherited, see description of AGLWGT_CMAKE_CONFIGURE_ARGS above
    # for more details.
    cmake_config_args = d.getVar("AGLWGT_CMAKE_CONFIGURE_ARGS")
    if bb.data.inherits_class("cmake", d) and cmake_config_args:
        d.appendVar("AGLWGT_EXTRA_BUILD_ARGS", ' CONFIGURE_ARGS="' + cmake_config_args + '"')
        d.appendVarFlag("AGLWGT_EXTRA_BUILD_ARGS", "vardeps", " AGLWGT_CMAKE_CONFIGURE_ARGS")
}

# Placeholder to keep things like externalsrc that prefunc or append
# do_configure working as expected.
aglwgt_do_configure() {
    true
}

aglwgt_do_compile() {
    bldcmd=${S}/autobuild/agl/autobuild
    if [ ! -x "$bldcmd" ]; then
        bbfatal "Missing autobuild/agl/autobuild script"
    fi

    if [ "${S}" != "${B}" ]; then
        rm -rf ${B}
        mkdir -p ${B}
        cd ${B}
    fi

    $bldcmd package BUILD_DIR=${B}/build-release ${AGLWGT_EXTRA_BUILD_ARGS}
    $bldcmd package-debug BUILD_DIR_DEBUG=${B}/build-debug ${AGLWGT_EXTRA_BUILD_ARGS}

    if echo ${BPN} | grep -q '^agl-service-' || [ "${AGLWGT_HAVE_TESTS}" = "1" ]; then
        # Only try building the test widget if there's source for it, to avoid spurious errors
        if [ -f ${S}/test/CMakeLists.txt ]; then
            $bldcmd package-test BUILD_DIR_TEST=${B}/build-test ${AGLWGT_EXTRA_BUILD_ARGS}
        fi

        # The coverage widget should always build
        $bldcmd package-coverage BUILD_DIR_COVERAGE=${B}/build-coverage ${AGLWGT_EXTRA_BUILD_ARGS}
    fi
}

POST_INSTALL_LEVEL ?= "10"
POST_INSTALL_SCRIPT ?= "${POST_INSTALL_LEVEL}-${PN}.sh"

EXTRA_WGT_POSTINSTALL ?= ""

aglwgt_do_install() {
    DEST=release
    if [ "${AGLWGT_AUTOINSTALL_${PN}}" = "0" ]; then
        DEST=manualinstall
    fi

    wgt="$(find ${B}/build-release -maxdepth 1 -name '*.wgt'| head -n 1)"
    if [ -n "$wgt" ]; then
        install -d ${D}/usr/AGL/apps/$DEST
        install -m 0644 $wgt ${D}/usr/AGL/apps/$DEST/
    else
        bbfatal "no package found in widget directory"
    fi

    for t in debug coverage test; do
        if [ "$(find ${B}/build-${t} -maxdepth 1 -name *-${t}.wgt)" ]; then
            install -d ${D}/usr/AGL/apps/${t}
            install -m 0644 ${B}/build-${t}/*-${t}.wgt ${D}/usr/AGL/apps/${t}/
        elif [ "$t" = "debug" ]; then
            # HTML5 widgets complicate things here, need to detect them and
            # not error out in that case.  ATM this requires looking in the
            # config.xml of the release widget.
            rm -rf ${B}/tmp
            unzip $wgt config.xml -d ${B}/tmp
            if [ -f ${B}/tmp/config.xml -a \
                 ! cat ${B}/tmp/config.xml | \
                     grep -q '^[[:space:]]*<content[[:space:]]\+src="[^\"]*"[[:space:]]\+type="text/html"' ]; then
                bbfatal "no package found in ${t} widget directory"
            fi
            rm -rf ${B}/tmp
        elif echo ${BPN} | grep -q '^agl-service-' || [ "${AGLWGT_HAVE_TESTS}" = "1" ]; then
            if [ "$t" = "coverage" -o -f ${S}/test/CMakeLists.txt ]; then
                bbfatal "no package found in ${t} widget directory"
            fi
        fi
    done

    if [ "${AGLWGT_AUTOINSTALL}" != "0" ]; then
        # For now assume autoinstall of the release versions
        rm -rf ${D}/usr/AGL/apps/autoinstall
        ln -sf release ${D}/usr/AGL/apps/autoinstall

        APP_FILES=""
        for file in ${D}/usr/AGL/apps/autoinstall/*.wgt; do
            APP_FILES="${APP_FILES} $(basename $file)";
        done
        install -d ${D}/${sysconfdir}/agl-postinsts
        cat > ${D}/${sysconfdir}/agl-postinsts/${POST_INSTALL_SCRIPT} <<EOF
#!/bin/sh -e
for file in ${APP_FILES}; do
    /usr/bin/afm-install install /usr/AGL/apps/autoinstall/\$file
done
sync
${EXTRA_WGT_POSTINSTALL}
EOF
        chmod a+x ${D}/${sysconfdir}/agl-postinsts/${POST_INSTALL_SCRIPT}
    fi
}

PACKAGES += "${PN}-test ${PN}-debug ${PN}-coverage"

FILES_${PN} += " \
    /usr/AGL/apps/release/*.wgt \
    /usr/AGL/apps/autoinstall \
    /usr/AGL/apps/manualinstall \
    ${sysconfdir}/agl-postinsts/${POST_INSTALL_SCRIPT} \
"
FILES_${PN}-test = "/usr/AGL/apps/test/*.wgt"
FILES_${PN}-debug = "/usr/AGL/apps/debug/*.wgt"
FILES_${PN}-coverage = "/usr/AGL/apps/coverage/*.wgt"

# Test widgets need the parent widget and the test framework
RDEPENDS_${PN}-test = "${PN} afb-test"

EXPORT_FUNCTIONS do_configure do_compile do_install

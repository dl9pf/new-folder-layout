# Copyright (c) 2018 LG Electronics, Inc.

SUMMARY = "Chromium webruntime for webOS"
AUTHOR = "Lokesh Kumar Goel <lokeshkumar.goel@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0 & BSD-3-Clause & LGPL-2.0 & LGPL-2.1"
LIC_FILES_CHKSUM = "\
    file://src/LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d \
    file://src/third_party/blink/renderer/core/LICENSE-LGPL-2;md5=36357ffde2b64ae177b2494445b79d21 \
    file://src/third_party/blink/renderer/core/LICENSE-LGPL-2.1;md5=a778a33ef338abbaf8b8a7c36b6eec80 \
"

require gn-utils.inc

inherit gettext qemu pythonnative

DEPENDS = "virtual/gettext wayland wayland-native pixman freetype glib-2.0 fontconfig openssl pango cairo icu libxkbcommon libexif dbus pciutils udev libcap alsa-lib virtual/egl elfutils-native libdrm atk gperf-native gconf nss nss-native nspr nspr-native bison-native qemu-native"

PROVIDES = "${BROWSER_APPLICATION}"

SRC_URI = "\
    git://github.com/igalia/${BPN};branch=WIP@39.agl.compositor;protocol=https;rev=${SRCREV_chromium68};name=chromium68 \
    git://github.com/webosose/v8;destsuffix=git/src/v8;rev=${SRCREV_v8};name=v8 \
    file://v8-qemu-wrapper.patch \
"
SRCREV_chromium68 = "61e96d1ee5dfc2461697457d287bf922d8a03d9a"
SRCREV_v8 = "1e3af71f1ff3735e8a5b639c48dfca63a7b8a647"

# we don't include SRCPV in PV, so we have to manually include SRCREVs in do_fetch vardeps
do_fetch[vardeps] += "SRCREV_v8"
SRCREV_FORMAT = "main_v8"

S = "${WORKDIR}/git"

SRC_DIR = "${S}/src"
OUT_DIR = "${WORKDIR}/build"
BUILD_TYPE = "Release"

B = "${OUT_DIR}/${BUILD_TYPE}"

WEBRUNTIME_BUILD_TARGET = "webos:weboswebruntime"
BROWSER_APP_BUILD_TARGET = "chrome"
BROWSER_APPLICATION = "chromium68-browser"
BROWSER_APPLICATION_DIR = "/opt/chromium68"

TARGET = "${WEBRUNTIME_BUILD_TARGET} ${BROWSER_APP_BUILD_TARGET}"

# Skip do_install_append of webos_system_bus. It is not compatible with this component.
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

PACKAGECONFIG ?= "jumbo use-upstream-wayland"

# Options to enable debug-webcore build.
# Add the following line to local.conf (or local.dev.inc) to enable them:
#   PACKAGECONFIG_append_pn-chromium68 = " debug-webcore"
# Other debug options are controlled by sections later in this file
PACKAGECONFIG[debug-webcore] = "remove_webcore_debug_symbols=false,remove_webcore_debug_symbols=true"

# Set a default value for jumbo file merge of 8. This should be good for build
# servers and workstations with a big number of cores. In case build is
# happening in a machine with less cores but still enough RAM a good value could
# be 50.
JUMBO_FILE_MERGE_LIMIT="8"
PACKAGECONFIG[jumbo] = "use_jumbo_build=true jumbo_file_merge_limit=${JUMBO_FILE_MERGE_LIMIT}, use_jumbo_build=false"

PACKAGECONFIG[lttng] = "use_lttng=true,use_lttng=false,lttng-ust,lttng-tools lttng-modules babeltrace"

# Chromium can use v4l2 device for hardware accelerated video decoding on such boards as Renesas R-car M3, for example.
# In case of R-car m3, additional patches are required for gstreamer and v4l2apps.
# See https://github.com/igel-oss/meta-browser-hwdecode/tree/igalia-chromium71.
PACKAGECONFIG[use-linux-v4l2] = "use_v4l2_codec=true use_v4lplugin=true use_linux_v4l2_only=true"

PACKAGECONFIG[use-upstream-wayland] = " \
        ozone_platform_wayland_external=false ozone_platform_wayland=true \
        use_system_minigbm=true, \
        ozone_platform_wayland_external=true ozone_platform_wayland=false \
"

GN_ARGS = "\
    enable_memorymanager_webapi=false\
    ffmpeg_branding=\"Chrome\"\
    host_os=\"linux\"\
    ozone_auto_platforms=false\
    proprietary_codecs=true\
    target_os=\"linux\"\
    treat_warnings_as_errors=false\
    is_agl=true\
    use_cbe=true\
    is_chrome_cbe=true\
    use_cups=false\
    use_custom_libcxx=false\
    use_kerberos=false\
    use_neva_media=false\
    use_ozone=true\
    use_xkbcommon=true\
    use_pmlog=false\
    use_system_debugger_abort=true\
    use_webos_gpu_info_collector=false\
    ${PACKAGECONFIG_CONFARGS}\
"

# From Chromium's BUILDCONFIG.gn:
# Set to enable the official build level of optimization. This has nothing
# to do with branding, but enables an additional level of optimization above
# release (!is_debug). This might be better expressed as a tri-state
# (debug, release, official) but for historical reasons there are two
# separate flags.
# See also: https://groups.google.com/a/chromium.org/d/msg/chromium-dev/hkcb6AOX5gE/PPT1ukWoBwAJ
GN_ARGS += "is_debug=false is_official_build=true"

# is_cfi default value is true for x86-64 builds with is_official_build=true.
# As of M63, we explicitly need to set it to false, otherwise we fail the
# following assertion in //build/config/sanitizers/sanitizers.gni:
#   assert(!is_cfi || is_clang,
#          "is_cfi requires setting is_clang = true in 'gn args'")
GN_ARGS += "is_cfi=false"

# By default, passing is_official_build=true to GN causes its symbol_level
# variable to be set to "2". This means the compiler will be passed "-g2" and
# we will end up with a very large chrome binary (around 5Gb as of M58)
# regardless of whether DEBUG_BUILD has been set or not. In addition, binutils,
# file and other utilities are unable to read a 32-bit binary this size, which
# causes it not to be stripped.
# The solution is two-fold:
# 1. Make sure -g is not passed on 32-bit architectures via DEBUG_FLAGS. -g is
#    the same as -g2. -g1 generates an 800MB binary, which is a lot more
#    manageable.
# 2. Explicitly pass symbol_level=0 to GN. This causes -g0 to be passed
#    instead, so that if DEBUG_BUILD is not set GN will not create a huge debug
#    binary anyway. Since our compiler flags are passed after GN's, -g0 does
#    not cause any issues if DEBUG_BUILD is set, as -g1 will be passed later.
DEBUG_FLAGS_remove_arm = "-g"
DEBUG_FLAGS_append_arm = "-g1"
DEBUG_FLAGS_remove_x86 = "-g"
DEBUG_FLAGS_append_x86 = "-g1"
GN_ARGS += "symbol_level=0"

# We do not want to use Chromium's own Debian-based sysroots, it is easier to
# just let Chromium's build system assume we are not using a sysroot at all and
# let Yocto handle everything.
GN_ARGS += "use_sysroot=false"

# Toolchains we will use for the build. We need to point to the toolchain file
# we've created, set the right target architecture and make sure we are not
# using Chromium's toolchain (bundled clang, bundled binutils etc).
GN_ARGS += "\
        custom_toolchain=\"//build/toolchain/yocto:yocto_target\" \
        gold_path=\"\" \
        host_toolchain=\"//build/toolchain/yocto:yocto_native\" \
        is_clang=${@is_default_cc_clang(d)} \
        clang_base_path=\"${@clang_install_path(d)}\" \
        clang_use_chrome_plugins=false \
        linux_use_bundled_binutils=false \
        target_cpu=\"${@gn_target_arch_name(d)}\" \
        v8_snapshot_toolchain=\"//build/toolchain/yocto:yocto_target\" \
"

# ARM builds need special additional flags (see ${S}/build/config/arm.gni).
# If we do not pass |arm_arch| and friends to GN, it will deduce a value that
# will then conflict with TUNE_CCARGS and CC.
# Note that as of M61 in some corner cases parts of the build system disable
# the "compiler_arm_fpu" GN config, whereas -mfpu is always passed via ${CC}.
# We might want to rework that if there are issues in the future.
def get_compiler_flag(params, param_name, d):
    """Given a sequence of compiler arguments in |params|, returns the value of
    an option |param_name| or an empty string if the option is not present."""
    for param in params:
      if param.startswith(param_name):
        return param.split('=')[1]
    return ''

ARM_FLOAT_ABI = "${@bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', 'hard', 'softfp', d)}"
ARM_FPU = "${@get_compiler_flag(d.getVar('TUNE_CCARGS').split(), '-mfpu', d)}"
ARM_TUNE = "${@get_compiler_flag(d.getVar('TUNE_CCARGS').split(), '-mcpu', d)}"
ARM_VERSION_aarch64 = "8"
ARM_VERSION_armv7a = "7"
ARM_VERSION_armv7ve = "7"
ARM_VERSION_armv6 = "6"

# GN computes and defaults to it automatically where needed
# forcing it from cmdline breaks build on places where it ends up
# overriding what GN wants
TUNE_CCARGS_remove = "-mthumb"

GN_ARGS_append_arm = " \
        arm_float_abi=\"${ARM_FLOAT_ABI}\" \
        arm_fpu=\"${ARM_FPU}\" \
        arm_tune=\"${ARM_TUNE}\" \
        arm_version=${ARM_VERSION} \
"
# tcmalloc's atomicops-internals-arm-v6plus.h uses the "dmb" instruction that
# is not available on (some?) ARMv6 models, which causes the build to fail.
GN_ARGS_append_armv6 += 'use_allocator="none"'
# The WebRTC code fails to build on ARMv6 when NEON is enabled.
# https://bugs.chromium.org/p/webrtc/issues/detail?id=6574
GN_ARGS_append_armv6 += 'arm_use_neon=false'

# Disable glibc shims on musl
# tcmalloc does not play well with musl as of M62 (and possibly earlier).
# https://github.com/gperftools/gperftools/issues/693
GN_ARGS_append_libc-musl = ' use_allocator_shim=false'

# V8's JIT infrastructure requires binaries such as mksnapshot and
# mkpeephole to be run in the host during the build. However, these
# binaries must have the same bit-width as the target (e.g. a x86_64
# host targeting ARMv6 needs to produce a 32-bit binary). Instead of
# depending on a third Yocto toolchain, we just build those binaries
# for the target and run them on the host with QEMU.
python do_create_v8_qemu_wrapper () {
    """Creates a small wrapper that invokes QEMU to run some target V8 binaries
    on the host."""
    qemu_libdirs = [d.expand('${STAGING_DIR_HOST}${libdir}'),
                    d.expand('${STAGING_DIR_HOST}${base_libdir}')]
    qemu_cmd = qemu_wrapper_cmdline(d, d.getVar('STAGING_DIR_HOST', True),
                                    qemu_libdirs)
    wrapper_path = d.expand('${B}/v8-qemu-wrapper.sh')
    with open(wrapper_path, 'w') as wrapper_file:
        wrapper_file.write("""#!/bin/sh

# This file has been generated automatically.
# It invokes QEMU to run binaries built for the target in the host during the
# build process.

%s "$@"
""" % qemu_cmd)
    os.chmod(wrapper_path, 0o755)
}
do_create_v8_qemu_wrapper[dirs] = "${B}"
addtask create_v8_qemu_wrapper after do_patch before do_configure

python do_write_toolchain_file () {
    """Writes a BUILD.gn file for Yocto detailing its toolchains."""
    toolchain_dir = d.expand("${S}/src/build/toolchain/yocto")
    bb.utils.mkdirhier(toolchain_dir)
    toolchain_file = os.path.join(toolchain_dir, "BUILD.gn")
    write_toolchain_file(d, toolchain_file)
}
addtask write_toolchain_file after do_patch before do_configure

# More options to speed up the build
GN_ARGS += "\
    enable_nacl=false\
    disable_ftp_support=true\
    enable_print_preview=false\
    enable_remoting=false\
    use_glib=true\
    use_gnome_keyring=false\
    use_pulseaudio=false\
"

# Respect ld-is-gold in DISTRO_FEATURES when enabling gold
# Similar patch applied in meta-browser
# http://patchwork.openembedded.org/patch/77755/
EXTRA_OEGN_GOLD = "${@bb.utils.contains('DISTRO_FEATURES', 'ld-is-gold', 'use_gold=true', 'use_gold=false', d)}"
GN_ARGS += "${EXTRA_OEGN_GOLD}"

# Doesn't build for armv[45]*
COMPATIBLE_MACHINE = "(-)"
COMPATIBLE_MACHINE_aarch64 = "(.*)"
COMPATIBLE_MACHINE_armv6 = "(.*)"
COMPATIBLE_MACHINE_armv7a = "(.*)"
COMPATIBLE_MACHINE_armv7ve = "(.*)"
COMPATIBLE_MACHINE_x86 = "(.*)"
COMPATIBLE_MACHINE_x86-64 = "(.*)"

#CHROMIUM_PLUGINS_PATH = "${libdir}"
CBE_DATA_PATH = "${libdir}/cbe"
CBE_DATA_LOCALES_PATH = "${CBE_DATA_PATH}/locales"

# The text relocations are intentional -- see comments in [GF-52468]
# TODO: check if we need INSANE_SKIP on ldflags
INSANE_SKIP_${PN} = "textrel ldflags"


do_compile[progress] = "outof:^\[(\d+)/(\d+)\]\s+"
do_compile() {
    if [ ! -f ${OUT_DIR}/${BUILD_TYPE}/build.ninja ]; then
         do_configure
    fi

    export PATH="${S}/depot_tools:$PATH"
    ${S}/depot_tools/ninja -v -C ${OUT_DIR}/${BUILD_TYPE} ${TARGET}
}

do_configure() {
    configure_env
}

configure_env() {
    export GYP_CHROMIUM_NO_ACTION=1
    export PATH="${S}/depot_tools:$PATH"

    GN_ARGS="${GN_ARGS}"
    echo GN_ARGS is ${GN_ARGS}
    echo BUILD_TARGETS are ${TARGET}
    cd ${SRC_DIR}
    gn gen ${OUT_DIR}/${BUILD_TYPE} --args="${GN_ARGS}"
}

WINDOW_SIZE ?= "1920,1080"

configure_browser_settings() {
    USER_AGENT="Mozilla/5.0 (Linux; NetCast; U) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/${CHROMIUM_VERSION} Safari/537.31"
    echo "${USER_AGENT}" > ${D_DIR}/user_agent_conf
    #We can replace below WINDOW_SIZE values from build configuration if available
    #echo "${WINDOW_SIZE}" > ${D_DIR}/window_size_conf
}

install_chromium_browser() {
    D_DIR=${D}${BROWSER_APPLICATION_DIR}
    install -d ${D_DIR}

    # Install browser files
     if [ -e "${SRC_DIR}/webos/install" ]; then
         cd ${OUT_DIR}/${BUILD_TYPE}
         xargs --arg-file=${SRC_DIR}/webos/install/default_browser/binary.list cp -R --no-dereference --preserve=mode,links -v --target-directory=${D_DIR}
         cd ${SRC_DIR}
         xargs --arg-file=${SRC_DIR}/webos/install/default_browser/runtime.list cp -R --no-dereference --preserve=mode,links -v --target-directory=${D_DIR}
     fi

    # AGL does not have PMLOG
    sed -i.bak s/PmLogCtl.*// ${D_DIR}/run_webbrowser

    # To execute chromium in JAILER, Security Part needs permissions change
    # run_webbrowser: Script file for launching chromium
    chmod -v 755 ${D_DIR}/chrome
    chmod -v 755 ${D_DIR}/kill_webbrowser
    chmod -v 755 ${D_DIR}/run_webbrowser

    configure_browser_settings
}

install_webruntime() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}/${BPN}
    install -d ${D}${CBE_DATA_PATH}
    install -d ${D}${CBE_DATA_LOCALES_PATH}

    # Install webos webview files
    if [ -e "${SRC_DIR}/webos/install" ]; then
        cd ${SRC_DIR}
        xargs --arg-file=${SRC_DIR}/webos/install/weboswebruntime/staging_inc.list cp --parents --target-directory=${D}${includedir}/${BPN}

        cd ${OUT_DIR}/${BUILD_TYPE}

        cp libcbe.so ${D}${libdir}/
        if [ "${WEBOS_LTTNG_ENABLED}" = "1" ]; then
          # use bindir if building non-cbe
          cp libchromium_lttng_provider.so ${D}${libdir}/
        fi
        xargs --arg-file=${SRC_DIR}/webos/install/weboswebruntime/binary.list cp --parents --target-directory=${D}${CBE_DATA_PATH}
        cat ${SRC_DIR}/webos/install/weboswebruntime/data_locales.list | xargs -I{} install -m 755 -p {} ${D}${CBE_DATA_LOCALES_PATH}
    fi

    # move this to separate mksnapshot-cross recipe once we figure out how to build just cross mksnapshot from chromium repository
    install -d ${D}${bindir_cross}
    gzip -c ${OUT_DIR}/${BUILD_TYPE}/${MKSNAPSHOT_PATH}mksnapshot > ${D}${bindir_cross}/${HOST_SYS}-mksnapshot.gz
}

do_install() {
    install_webruntime
    install_chromium_browser
}

WEBOS_SYSTEM_BUS_DIRS_LEGACY_BROWSER_APPLICATION = " \
    ${webos_sysbus_prvservicesdir}/${BROWSER_APPLICATION}.service \
    ${webos_sysbus_pubservicesdir}/${BROWSER_APPLICATION}.service \
    ${webos_sysbus_prvrolesdir}/${BROWSER_APPLICATION}.json \
    ${webos_sysbus_pubrolesdir}/${BROWSER_APPLICATION}.json \
"

SYSROOT_DIRS_append = " ${bindir_cross}"

PACKAGES_prepend = " \
    ${PN}-cross-mksnapshot \
    ${BROWSER_APPLICATION} \
"

FILES_${BROWSER_APPLICATION} += " \
    ${BROWSER_APPLICATION_DIR} \
    ${WEBOS_SYSTEM_BUS_DIRS_LEGACY_BROWSER_APPLICATION} \
"

RDEPENDS_${BROWSER_APPLICATION} += "${PN}"

VIRTUAL-RUNTIME_gpu-libs ?= ""
RDEPENDS_${PN} += "${VIRTUAL-RUNTIME_gpu-libs}"

# The text relocations are intentional -- see comments in [GF-52468]
# TODO: check if we need INSANE_SKIP on ldflags
INSANE_SKIP_${BROWSER_APPLICATION} += "libdir ldflags textrel"

FILES_${PN} = " \
    ${libdir}/*.so \
    ${CBE_DATA_PATH}/* \
    ${libdir}/${BPN}/*.so \
    ${WEBOS_SYSTEM_BUS_DIRS} \
"

FILES_${PN}-dev = " \
    ${includedir} \
"

FILES_${PN}-cross-mksnapshot = "${bindir_cross}/${HOST_SYS}-mksnapshot.gz"

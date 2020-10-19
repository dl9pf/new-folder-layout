FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

DEPENDS += "swig-native"

SRC_URI +=" \
            file://0001-Set-up-environment-for-OSTree-integration.patch \
          "

# fix after default security flags in poky
TOOLCHAIN_OPTIONS_append = "${SECURITY_NOPIE_CFLAGS}"

do_compile_prepend_qemux86-64 () {
  export BUILD_ROM=y
}
UBOOT_SUFFIX_qemux86-64 = "rom"

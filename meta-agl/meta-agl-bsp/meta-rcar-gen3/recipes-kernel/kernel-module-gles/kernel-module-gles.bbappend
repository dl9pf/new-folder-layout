require checksum_control.inc

module_do_compile_prepend() {
    cd ${S}/build/linux/config/compilers
    cp aarch64-poky-linux.mk ${TARGET_SYS}.mk
}

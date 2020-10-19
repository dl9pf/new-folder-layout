OMX_LIBS = "mp3dec_lib"

do_configure_append() {
    for lib in ${OMX_LIBS}; do
        if ${@bb.utils.contains('DISTRO_FEATURES', '$lib', 'false', 'true', d)}; then
            lib="omx${lib%%_lib}"
            sed -i "/^\[$lib\]/,/^$/d" ${S}/config/rcar/gstomx.conf
        fi
    done
}

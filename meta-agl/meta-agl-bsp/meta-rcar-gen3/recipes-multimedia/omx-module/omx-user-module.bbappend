require checksum_control.inc

PROVIDES += "virtual/libomxil"

setup_build_tree() {
    for omxmc in ${OMX_COMMON_SRC} ${OMX_VIDEO_DEC_COMMON_SRC} ${OMX_VIDEO_ENC_COMMON_SRC}
    do
        tar xf ${WORKDIR}/${omxmc}.tar.bz2 -C ${WORKDIR}
        tar xf ${WORKDIR}/${omxmc}.tar.bz2 -C ${S} ${omxmc}/src --strip=2 
        tar xf ${WORKDIR}/${omxmc}.tar.bz2 -C ${S} ${omxmc}/include --strip=1
    done
}

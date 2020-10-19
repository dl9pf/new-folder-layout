FILESEXTRAPATHS_append := ":${THISDIR}/files"

SRC_URI_append_class-nativesdk = " \
    file://environment.d-cmake-agl.sh \
"

do_install_append_class-nativesdk() {
    install -m 644 ${WORKDIR}/environment.d-cmake-agl.sh ${D}${SDKPATHNATIVE}/environment-setup.d/cmake-agl.sh
}

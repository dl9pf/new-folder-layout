FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " \
        file://0001-cached-buffers-support.patch;patchdir=${WORKDIR}/git \
"

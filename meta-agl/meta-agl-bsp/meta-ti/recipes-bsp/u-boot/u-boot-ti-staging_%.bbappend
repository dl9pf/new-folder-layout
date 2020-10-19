FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PR_append = "_agl"

SRC_URI += " file://${UBOOT_ENV_BINARY}"

UBOOT_ENV = "uEnv"

do_deploy_append () {
    if [ "x${UBOOT_ENV}" != "x" ]
    then
        install ${WORKDIR}/${UBOOT_ENV_BINARY} ${DEPLOYDIR}/${UBOOT_ENV_IMAGE}
        ln -sf ${UBOOT_ENV_IMAGE} ${DEPLOYDIR}/${UBOOT_ENV_SYMLINK}
    fi
}


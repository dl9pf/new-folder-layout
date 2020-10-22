#Enable eglfs for QT based application

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# FIXME: Disabled as they do not apply against 5.13.2, and it is
#        unclear if they are still required for building dra7xx-evm,
#        which fails for what looks like a different reason.
#SRC_URI += " file://0001-fixed-eglfs_kms-fails-to-build.patch \
#             file://0002-fixed-invalid-conversion-from-EGLNativeDisplayType-to-void.patch \
#           "

PACKAGECONFIG_GL_append = "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', ' eglfs kms gbm', '', d)}"

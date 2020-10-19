# See meta-updater/conf/distro/sota.conf.inc
INHERIT += "sota"
DISTRO_FEATURES_append = " sota usrmerge"
DISTRO_FEATURES_NATIVE_append = " sota"
AGL_DEFAULT_INITRAMFS_FSTYPES = "cpio.gz"
# Netboot is not obeying usrmerge distro feature
# therefore it is not compatible with agl-sota
CONFLICT_DISTRO_FEATURES += "netboot"

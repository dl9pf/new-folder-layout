# enable security features (smack, cynagora) - required by Application Framework
OVERRIDES .= ":with-lsm-smack"
DISTRO_FEATURES_append = " smack xattr appfw"
DISTRO_FEATURES_NATIVE_append = " smack xattr"

# use tar-native to support SMACK extended attributes independently of host config
IMAGE_CMD_TAR = "tar --xattrs --xattrs-include='*'"
do_image_tar[depends] += "tar-replacement-native:do_populate_sysroot"
EXTRANATIVEPATH += "tar-native"

# security: enable ssh server in place of dropbear to support PAM on user sessions
IMAGE_FEATURES += "ssh-server-openssh"

# enforce copy of xattrs (to be removed, see SPEC-475)
PACKAGECONFIG_append_pn-shadow = " attr"
PACKAGECONFIG_append_pn-shadow-native = " attr"

# set the home directory for root
ROOT_HOME = "/home/0"

# include devel wgts in images
IMAGE_FEATURES_append = " agl-devel-wgt"

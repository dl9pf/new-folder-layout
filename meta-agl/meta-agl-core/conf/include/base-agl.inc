# AGL includes all kernel modules here for ease-of-use during development.
# Comment this out to be able to select the kernel modules yourself.
IMAGE_INSTALL_append = " kernel-modules"

# Likewise as we included all kernel modules by default in the filesystem,
# we do not need a separate tarball stored.
# Comment this out to receive the separate modules tarball again.
MODULE_TARBALL_DEPLOY ?= "0"

# Comment out below if want to use QtWebkit
PACKAGECONFIG_remove_pn-qtquick1 = "webkit"


ASSUME_PROVIDED_remove = "tar-native"

# Use static IDs to enable full-filesystem updates
USERADDEXTENSION = "useradd-staticids"
USERADD_ERROR_DYNAMIC = "error"
USERADD_GID_TABLES = "files/group"
USERADD_UID_TABLES = "files/passwd"


AGL_FEATURES += "aglcore"

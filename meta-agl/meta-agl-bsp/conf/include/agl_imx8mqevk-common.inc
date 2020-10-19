DISTRO_FEATURES_append = " agl-medium-arm-compiler"

# Force use of the community supported LTS kernel instead of the
# default linux-imx
IMX_DEFAULT_KERNEL_imx8mqevk = "linux-fslc-imx"

# For EVKB wifi support
MACHINE_FEATURES_append = " bcm4356"

# Disable meta-freescale package architecure mangling, as it causes
# issues with AGL's explicit setting of DEFAULTTUNE.
INHERIT_remove = "fsl-dynamic-packagearch"
MACHINE_SOCARCH = "${TUNE_PKGARCH}"

# Disable the meta-freescale weston-init bbappend as it conflicts
# with AGL's.
BBMASK = "meta-freescale/recipes-graphics/wayland/weston-init"

AGL_DEFAULT_INITRAMFS_FSTYPES := ""

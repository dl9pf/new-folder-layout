AGL_FEATURES_append = " agl-medium-arm-compiler"

# This disables the Freescale/NXP Vivante GPU driver and enables
# etnaviv support.
MACHINEOVERRIDES .= ":use-mainline-bsp"

# Disable meta-freescale package architecure mangling, as it causes
# issues with AGL's explicit setting of DEFAULTTUNE.
INHERIT_remove = "fsl-dynamic-packagearch"
MACHINE_SOCARCH = "${TUNE_PKGARCH}"

# Disable the meta-freescale weston-init bbappend as it conflicts
# with AGL's.
BBMASK = "meta-freescale/recipes-graphics/wayland/weston-init"

# Set CMA size large enough to work with AGL
UBOOT_EXTLINUX_KERNEL_ARGS_append = " cma=512M"

AGL_DEFAULT_INITRAMFS_FSTYPES := ""

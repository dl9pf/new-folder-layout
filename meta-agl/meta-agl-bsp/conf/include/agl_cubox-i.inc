DISTRO_FEATURES_append = " agl-medium-arm-compiler"

# This disables the Freescale/NXP Vivante GPU driver and enables
# etnaviv support.
MACHINEOVERRIDES .= ":use-mainline-bsp"

# Disable meta-freescale package architecure mangling, as it causes
# issues with AGL's explicit setting of DEFAULTTUNE.
INHERIT_remove = "fsl-dynamic-packagearch"
MACHINE_SOCARCH = "${TUNE_PKGARCH}"

# Include devicetrees for SOM 1.5 and Hummingboard 2 hardware
KERNEL_DEVICETREE_append = " \
	imx6dl-hummingboard-som-v15.dtb imx6q-hummingboard-som-v15.dtb \ 
	imx6dl-hummingboard2.dtb imx6q-hummingboard2.dtb \
	imx6dl-hummingboard2-som-v15.dtb imx6q-hummingboard2-som-v15.dtb \
"

# Disable the meta-freescale weston-init bbappend as it conflicts
# with AGL's.
BBMASK = "meta-freescale/recipes-graphics/wayland/weston-init"

# Set CMA size large enough to work with AGL
UBOOT_EXTLINUX_KERNEL_ARGS_append = " cma=512M"

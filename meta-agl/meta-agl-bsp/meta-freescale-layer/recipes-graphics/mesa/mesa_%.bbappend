FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-kmsro-add-imx-dcss.patch"

# These over-rides should use "use-mainline-bsp" instead when that
# becomes more workable for i.MX8 in upstream meta-freescale.

USE_OSMESA_ONLY_etnaviv = "no"

PACKAGECONFIG_append_etnaviv = " gallium etnaviv kmsro"

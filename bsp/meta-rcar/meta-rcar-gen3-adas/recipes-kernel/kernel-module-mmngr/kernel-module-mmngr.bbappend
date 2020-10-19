FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

MMNGR_CFG_eagle = "MMNGR_V3MSK"
MMNGR_CFG_v3msk = "MMNGR_V3MSK"
MMNGR_CFG_condor = "MMNGR_V3MSK"
MMNGR_CFG_v3mzf = "MMNGR_V3MSK"
MMNGR_CFG_v3hsk = "MMNGR_V3MSK"

SRC_URI_append = " \
    file://0002-mmngr-Add-V3MSK-board.patch \
    file://0003-mmngr-Add-kernelheap-cached-buffers-support.patch \
"

KERNEL_MODULE_AUTOLOAD += "mmngr"
KERNEL_MODULE_PROBECONF += "mmngr"
module_conf_mmngr = ""

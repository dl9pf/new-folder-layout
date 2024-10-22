# Released under the MIT license (see COPYING.MIT for the terms)
#
# SPDX-License-Identifier: MIT
#

SUMMARY = "Linux Kernel provided by NXP and supported by Community"
DESCRIPTION = "Linux Kernel provided by NXP as the part of release distribution. \
Main focus is set on i.MX Family Reference Boards. \
It includes support for many NXP Proprietary IPs (GPU, VPU, IPU). \
Latest stable Kernel patchlevel is applied and maintained by Community."

###############################################################################
# This recipe (and corresponding kernel repository and branch) receives updates
# from 3 different sources:
# 1. Stable [linux-5.4.y] branch updates of korg;
# 2. NXP-specific updates via branch [lf-5.4.y] shared via CodeAurora forum;
# 3. Critical patches, which are not (yet) integrated into either of 2 above
#    sources, but are required to be applied to the kernel tree.
#
# Therefore, there is a need to keep track on the patches which are introduced
# from every source. This could be achieved in this recipe by filling the
# below list with the information once the update is performed from any source.
#
# Once the critical patch gets merged into the stable branch, or NXP-specific
# patches would be covered by the tag - individual entries from sections below
# could be removed.
#
# ------------------------------------------------------------------------------
# 1. Stable (tag or SHA(s))
# ------------------------------------------------------------------------------
#    tag: v5.4.51
#
# ------------------------------------------------------------------------------
# 2. NXP-specific (tag or SHA(s))
# ------------------------------------------------------------------------------
#    tag: lf-5.4.y-1.0.0
#    40fe6bd54230 MLK-23844-2 ARM64: dts: freescale: imx8mm: correct VDDARM@1.6GHz
#    aec0c14ae61d MLK-23844-1: ARM64: dts: freescale: imx8mn-ddr4-evk: cleanup Rhom pmic
#    e8e35fd0e759 MLK-23275-2: ARM64: dts: freescale: imx8mn-ddr4-evk: correct ldo1/ldo2 voltage
#    975d8ab07267 MLK-23275-1: ARM64: dts: freescale: fsl-imx8mm-evk: correct ldo1/ldo2 voltage
#    4711466d535c MLK-23562: regulator: core: adjust supply voltage in bypass
#    bbbc26a40a9c MLK-13793-6 regulator: anatop: fix min dropout for bypass mode
#
# ------------------------------------------------------------------------------
# 3. Critical patches (SHA(s))
# ------------------------------------------------------------------------------
#    285869ec9087 ARM: dts: imx6ul-kontron-n6x1x-s: Remove an obsolete comment and fix indentation
#    f03791085754 ARM: dts: imx6ul-kontron-n6x1x-s: Add vbus-supply and overcurrent polarity to usb nodes
#    67e44952c670 ARM: dts: imx6ul-kontron-n6x1x: Add 'chosen' node with 'stdout-path'
#    d4b105f930e2 ARM: dts: Add support for two more Kontron evalkit boards 'N6311 S' and 'N6411 S'
#    e778467f65d0 ARM: dts: imx6ul-kontron-n6310-s: Move common nodes to a separate file
#    8b39f5e53831 ARM: dts: Add support for two more Kontron SoMs N6311 and N6411
#    3bee262233eb ARM: dts: imx6ul-kontron-n6310: Move common SoM nodes to a separate file
#    40a6a98b3f38 ARM: imx: Fix boot crash if ocotp is not found
#    8aad5031be00 ARM: imx: Correct ocotp id for serial number support of i.MX6ULL/ULZ SoCs
#    564409aa3b33 soc: imx-scu: Using existing serial_number instead of UID
#    0ee8e005bbfc soc: imx8: Using existing serial_number instead of UID
#    134cbb845fc7 ARM: imx: Add serial number support for i.MX6/7 SoCs
#
# NOTE to upgraders:
# This recipe should NOT collect individual patches, they should be applied to
# the linux-fslc kernel tree on the corresponding branch, and tracking
# information should be properly filled in above.
###############################################################################

include linux-fslc.inc

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

SRCBRANCH = "5.4-1.0.0-imx"
SRCREV = "9fb52fae75909b9ca83b2a04e824f97d2df85302"

# PV is defined in the base in linux-imx.inc file and uses the LINUX_VERSION definition
# required by kernel-yocto.bbclass.
#
# LINUX_VERSION define should match to the kernel version referenced by SRC_URI and
# should be updated once patchlevel is merged.
LINUX_VERSION = "5.4.51"

# Local version indicates the branch name in the NXP kernel tree where patches are collected from.
LOCALVERSION = "-lf-5.4.y"

DEFAULT_PREFERENCE = "1"

COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"

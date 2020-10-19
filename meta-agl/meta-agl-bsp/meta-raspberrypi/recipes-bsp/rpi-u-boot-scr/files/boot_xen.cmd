#
# SPDX-License-Identifier: MIT
#
# Copyright (c) 2020, MERA
#
# Author: Leonid Lazarev
#
# Xen Boot Script
#
# https://www.raspberrypi.org/documentation/configuration/device-tree.md
# We do not set fdt_addr, because device tree initially is loaded by raspberry pi firmware loader and the particular
# modification are performed. The prepared DTS is propagated to u-boot and this prepared device tree has to be reused.

setenv kernel_addr_r  0x00480000  # 16M
setenv xen_addr_r     0x00200000 # 2M

# Load xen to ${xen_addr_r}.
fatload mmc 0:1 ${xen_addr_r} /xen-@@MACHINE@@

#configure dom0
fdt addr ${fdt_addr}

#read prepared bootargs, rapsberry pi prepared initial list of the parameters for loading
fdt get value bootargs /chosen bootargs
fdt resize 8192

# add device type for raspberry
fdt set pcie0 device_type "pci"

fdt chosen
fdt set /chosen \#address-cells <1>
fdt set /chosen \#size-cells <1>

# Load Linux Image to ${kernel_addr_r}
fatload mmc 0:1 ${kernel_addr_r} /@@KERNEL_IMAGETYPE@@

# we load dom0 with 1512 MB of memory
fdt mknod /chosen dom0
fdt set /chosen xen,xen-bootargs "console=dtuart dtuart=/soc/serial@7e215040 sync_console dom0_mem=1512M bootscrub=0"
fdt set /chosen xen,dom0-bootargs "${bootargs}"

fdt set /chosen/dom0 compatible "xen,linux-zimage", "xen,multiboot-module"
fdt set /chosen/dom0 reg <${kernel_addr_r} 0x${filesize} >

@@KERNEL_BOOTCMD@@ ${xen_addr_r} - ${fdt_addr}

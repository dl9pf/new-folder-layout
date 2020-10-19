# DEFAULTTUNE overrides for AGL on aarch64

# We should not need the tuning include below as the BSP should include the right set already.
# A double inclusion would produce a warning. This include line is just for reference
# include conf/machine/include/arm/arch-armv8.inc

DEFAULTTUNE = "aarch64"
# not yet ready
#QB_SYSTEM_NAME ?= "qemu-system-aarch"
#QB_DEFAULT_KERNEL ?= "zImage"
#QB_DEFAULT_FSTYPE ?= "ext4"
#QB_MACHINE ?= "-machine foo"
#QB_CPU ?= "-cpu bar"

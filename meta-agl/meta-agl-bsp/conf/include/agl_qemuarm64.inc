# Configuration for serial console
#QB_KERNEL_CMDLINE_APPEND_append = " console=ttyAMA0,115200n8"

# All boot message will be off
QB_KERNEL_CMDLINE_APPEND_append = " quiet"

# Build updatable image. Only takes effect when sota.bbclass is inherited
DISTRO_FEATURES_append = " sota"

# Root device
ROOT_VM = "root=PARTUUID=${DISK_SIGNATURE}-02"

# Over-ride setting in oe-core's qemuarm64.conf
QB_MEM_qemuarm64 = "-m 2048"

# Use our own wks file
WKS_FILE="directdisk.wks.in"

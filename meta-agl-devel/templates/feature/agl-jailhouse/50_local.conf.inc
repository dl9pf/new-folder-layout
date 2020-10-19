#### JAILHOUSE 
IMAGE_INSTALL_append = " jailhouse jailhouse-demos"
PREFERRED_VERSION_linux-raspberrypi = "5.4%"

RPI_KERNEL_DEVICETREE_OVERLAYS_append = " overlays/jailhouse-memory.dtbo"

#Qemu Boot Config
# qemu flags for jailhouse
QB_OPT_APPEND_append = " -device intel-iommu,intremap=on,x-buggy-eim=on -device intel-hda,addr=1b.0 -device hda-duplex"
QB_MEM = "-m 1G"

# use override with higher priority than x86-64
QB_MACHINE_qemux86-64 = "-machine q35,kernel_irqchip=split -smp 4"
QB_CPU_qemux86-64 = "-enable-kvm"
QB_CPU_KVM_qemux86-64 = "-cpu host,-kvm-pv-eoi,-kvm-pv-ipi,-kvm-asyncpf,-kvm-steal-time,-kvmclock,-invpcid,-hypervisor"

QB_KERNEL_CMDLINE_APPEND_append = " intel_iommu=off memmap=82M\$0x22000000"

QB_KERNEL_ROOT = "/dev/vda"
QB_SERIAL_OPT = "-serial none -serial stdio -serial telnet:localhost:4321,server,nowait"


# jailhouse needs ttyS1 in the agl-qemu.cell as debug console.
# restrict the SERIAL_CONSOLES to ttyS0 only
SERIAL_CONSOLES = "115200;ttyS0"
KERNEL_CONSOLE = "ttyS0"
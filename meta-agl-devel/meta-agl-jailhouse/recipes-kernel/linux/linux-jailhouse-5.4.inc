FILESEXTRAPATHS_prepend := "${THISDIR}/linux:"

SRC_URI_append = " file://jailhouse.cfg"

SRC_URI_append = " \
file://0001-x86-jailhouse-Improve-setup-data-version-comparison.patch \
file://0002-x86-jailhouse-Only-enable-platform-UARTs-if-availabl.patch \
file://0003-jailhouse-Add-simple-debug-console-via-the-hyperviso.patch \
file://0004-arm-Export-__boot_cpu_mode-for-use-in-Jailhouse-driv.patch \
file://0005-mm-Re-export-ioremap_page_range.patch \
file://0006-arm-arm64-export-__hyp_stub_vectors.patch \
file://0007-x86-Export-lapic_timer_period.patch \
file://0008-arm64-dts-marvell-armada-37xx-Set-pci-domain.patch \
file://0009-arm64-dts-marvell-armada-8030-mcbin-Set-pci-domain.patch \
file://0010-uio-Enable-read-only-mappings.patch \
file://0011-ivshmem-Add-header-file.patch \
file://0012-uio-Add-driver-for-inter-VM-shared-memory-device.patch \
file://0013-ivshmem-net-virtual-network-device-for-Jailhouse.patch \
file://0014-ivshmem-net-Map-shmem-region-as-RAM.patch \
file://0015-ivshmem-net-fix-race-in-state-machine.patch \
file://0016-ivshmem-net-Remove-unused-variable.patch \
file://0017-ivshmem-net-Enable-INTx.patch \
file://0018-ivshmem-net-Improve-identification-of-resources.patch \
file://0019-ivshmem-net-Switch-to-reset-state-on-each-net-stop-a.patch \
file://0020-ivshmem-net-Add-ethtool-register-dump.patch \
file://0021-ivshmem-net-Fix-stuck-state-machine-during-setup.patch \
file://0022-ivshmem-net-Switch-to-relative-descriptor-addresses.patch \
file://0023-ivshmem-net-Switch-to-pci_alloc_irq_vectors.patch \
file://0024-ivshmem-net-fill-in-and-check-used-descriptor-chain-.patch \
file://0025-ivshmem-net-slightly-improve-debug-output.patch \
file://0026-ivshmem-net-set-and-check-descriptor-flags.patch \
file://0027-ivshmem-net-add-MAC-changing-interface.patch \
file://0028-ivshmem-net-Silence-compiler-warning.patch \
file://0029-ivshmem-net-Fix-bogus-transition-to-RESET-state.patch \
file://0030-ivshmem-net-Refactor-and-comment-ivshm_net_state_cha.patch \
file://0031-ivshmem-net-Switch-to-netdev_xmit_more-helper.patch \
file://0032-ivshmem-net-Adjust-to-reworked-version-of-ivshmem-in.patch \
"


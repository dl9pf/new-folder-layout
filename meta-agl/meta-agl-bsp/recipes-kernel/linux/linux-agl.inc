FILESEXTRAPATHS_prepend := "${THISDIR}/linux:"

DEPENDS += "kern-tools-native"

# returns all the elements from the src uri that are .cfg files
def find_cfgs(d):
    sources=src_patches(d, True)
    sources_list=[]
    for s in sources:
        if s.endswith('.cfg'):
            sources_list.append(s)

    return sources_list

do_configure_append () {
    [ ! -f .config ] && cp -a ${WORKDIR}/defconfig .config
    # Need to clear CONFIG_LSM for 5.1+ kernels to ensure it'll get
    # regenerated to reflect configuration changes (e.g. SMACK).
    sed -i '/^CONFIG_LSM/d' .config
    merge_config.sh -m .config ${@" ".join(find_cfgs(d))}
    yes '' | make oldconfig
}

# Extra configuration options for the AGL kernel
SRC_URI_append = "\
            file://can-bus.cfg \
            file://usb.cfg \
            file://uvc.cfg \
            file://joystick.cfg \
            file://fanotify.cfg \
            file://uinput.cfg \
            file://hid.cfg \
            file://drm.cfg \
            file://btusb.cfg \
            file://usbaudio.cfg \
            file://usbmodem.cfg \
            file://i2c-led.cfg \
            file://nfc.cfg \
            file://overlayfs.cfg \
        "

SRC_URI_append = " ${@bb.utils.contains('AGL_XEN_GUEST_WANTED','1','file://xen_domu.cfg','',d)}"

KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/can-bus.cfg"
# Enable support for usb video class for usb camera devices
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/uvc.cfg"
# Enable support for joystick devices
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/joystick.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fanotify.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/uinput.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/hid.cfg"
# Enable DRM support for graphics
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/drm.cfg"
# Enable Bluetooth USB devices
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/btusb.cfg"
# Enable USB audio devices
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/usbaudio.cfg"
# Enable I2C and LED for demontrator
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/i2c-led.cfg"
# Enable NFC devices
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/nfc.cfg"
# Enable overlayfs filesystem support
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/overlayfs.cfg"
# Enable XEN support of the guest
KERNEL_CONFIG_FRAGMENTS_append = " ${@bb.utils.contains('AGL_XEN_GUEST_WANTED','1','${WORKDIR}/xen_domu.cfg','',d)}"

# Enable required features for the agl-netboot feature
SRC_URI_append_netboot = " file://nbd.cfg \
	file://ramdisk.cfg"
KERNEL_CONFIG_FRAGMENTS_append_netboot = " ${WORKDIR}/nbd.cfg ${WORKDIR}/ramdisk.cfg"

# Enable support for TP-Link TL-W722N USB Wifi adapter (rev 1 and 2)
SRC_URI_append = " file://wifi.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/wifi.cfg"

# Enable support for RTLSDR
SRC_URI_append = " file://rtl_sdr.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/rtl_sdr.cfg"

# Additional drivers for virtual machines
# OVERRIDES save us some c'n'p below ...
OVERRIDES_prepend_qemux86 = "virtualmachine:"
OVERRIDES_prepend_qemux86-64 = "virtualmachine:"
SRC_URI_append_virtualmachine = " file://vbox-vmware-sata.cfg"
KERNEL_CONFIG_FRAGMENTS_append_virtualmachine = " ${WORKDIR}/vbox-vmware-sata.cfg"

# Extra configuration for using qemux86-64 image on physical hardware
SRC_URI_append_qemux86-64 = "\
    file://x86-extra-graphic-devices.cfg \
    file://x86-net-devices.cfg \
    file://x86-security-tpm.cfg \
    file://x86-usb-devices.cfg \
    file://x86-upsquare.cfg \
"
KERNEL_CONFIG_FRAGMENTS_append_qemux86-64 = "\
    ${WORKDIR}/x86-extra-graphic-devices.cfg \
    ${WORKDIR}/x86-net-devices.cfg \
    ${WORKDIR}/x86-security-tpm.cfg \
    ${WORKDIR}/x86-usb-devices.cfg \
    ${WORKDIR}/x86-upsquare.cfg \
"

# Enable support for smack
KERNEL_CONFIG_FRAGMENTS_append_with-lsm-smack = "\
       ${WORKDIR}/audit.cfg \
       ${WORKDIR}/smack.cfg \
       ${WORKDIR}/smack-default-lsm.cfg \
"

# ALSA support and other sound related configuration
SRC_URI_append = " file://sound.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/sound.cfg"

# iio-dummy-device support for test iiodevice
SRC_URI_append = " file://iiodevice.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/iiodevice.cfg"

# external rtc support via e.g. http://wiki.seeedstudio.com/Grove-RTC/
SRC_URI_append = " file://rtc.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/rtc.cfg"

# netfilter options
SRC_URI_append = " file://netfilter.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/netfilter.cfg"

# Enable support for SystemTap
SRC_URI_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'agl-devel', 'file://systemtap.cfg', '', d)}"
KERNEL_CONFIG_FRAGMENTS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'agl-devel', '${WORKDIR}/systemtap.cfg', '', d)}"

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

require recipes-kernel/linux/linux-agl.inc

SRC_URI_append = "\
    ${@oe.utils.conditional('USE_FAYTECH_MONITOR', '1', 'file://0002-faytech-fix-rpi.patch', '', d)} \
"
#take in account that linux under xen should use the hvc0 console
SERIAL_OPTION = "${@bb.utils.contains('AGL_XEN_WANTED','1','hvc0','115200;ttyS0',d)}"
SERIAL = "${@oe.utils.conditional("ENABLE_UART", "1", "console=${SERIAL_OPTION}", "", d)}"

CMDLINE_DEBUG = ""

#XEN related option
CMDLINE_append = ' ${@bb.utils.contains('AGL_XEN_WANTED','1','clk_ignore_unused','',d)}'

#workaround for crash during brcmfmac loading. Disable it at this moment
CMDLINE_append = ' ${@bb.utils.contains('AGL_XEN_WANTED','1','modprobe.blacklist=brcmfmac','',d)}'

CMDLINE_append = " usbhid.mousepoll=0"

# Add options to allow CMA to operate
CMDLINE_append = ' ${@oe.utils.conditional("ENABLE_CMA", "1", "coherent_pool=6M smsc95xx.turbo_mode=N", "", d)}'

KERNEL_MODULE_AUTOLOAD += "snd-bcm2835"
KERNEL_MODULE_AUTOLOAD += "hid-multitouch"

RDEPENDS_${PN} += "kernel-module-snd-bcm2835"
PACKAGES += "kernel-module-snd-bcm2835"

# Enable support for usb video class for usb camera devices
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/uvc.cfg"

# Enable support for joystick devices
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/joystick.cfg"

# Enable support for Pi foundation touchscreen
SRC_URI_append = " file://raspberrypi-panel.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/raspberrypi-panel.cfg"

# Enable bt hci uart
SRC_URI_append = " file://raspberrypi-hciuart.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/raspberrypi-hciuart.cfg"

# ENABLE NETWORK (built-in)
SRC_URI_append = " file://raspberrypi_network.cfg"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/raspberrypi_network.cfg"

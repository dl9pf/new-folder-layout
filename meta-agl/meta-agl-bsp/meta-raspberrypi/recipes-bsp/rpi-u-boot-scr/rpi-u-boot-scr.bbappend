FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append =" \
    ${@bb.utils.contains('AGL_XEN_WANTED','1',' file://boot_xen.cmd','',d)} \
"

do_compile_append() {

 # if xen feature is activated we overwirte the boot script with xen specific one
   if [ "${AGL_XEN_WANTED}" = "1" ]; then
        sed -e 's/@@KERNEL_IMAGETYPE@@/${KERNEL_IMAGETYPE}/' \
      	    -e 's/@@KERNEL_BOOTCMD@@/${KERNEL_BOOTCMD}/' \
    	    -e 's/@@MACHINE@@/${MACHINE}/' \
              "${WORKDIR}/boot_xen.cmd" > "${WORKDIR}/boot.cmd"

     	mkimage -A arm -T script -C none -n "Boot script" -d "${WORKDIR}/boot.cmd" boot.scr
   fi
}
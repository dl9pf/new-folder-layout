
do_install_append() {
   # Needed for wayland-0 socket access and memfd usage
   echo "~APP~ System::Weston rw" >> ${D}${datadir}/security-manager/policy/app-rules-template.smack
   echo "System::Weston ~APP~ rw" >> ${D}${datadir}/security-manager/policy/app-rules-template.smack
}

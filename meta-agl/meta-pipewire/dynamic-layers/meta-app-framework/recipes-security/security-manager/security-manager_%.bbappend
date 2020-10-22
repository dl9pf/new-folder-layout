
do_install_append() {
   echo "~APP~ System::Pipewire rw" >> ${D}${datadir}/security-manager/policy/app-rules-template.smack
}

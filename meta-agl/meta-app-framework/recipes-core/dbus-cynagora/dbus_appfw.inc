FILESEXTRAPATHS_prepend := "${THISDIR}/dbus-cynagora:"

SRC_URI_append_class-target = "\
   file://0001-Integration-of-Cynara-asynchronous-security-checks.patch \
   file://0002-Disable-message-dispatching-when-send-rule-result-is.patch \
   file://0003-Handle-unavailability-of-policy-results-for-broadcas.patch \
   file://0004-Add-own-rule-result-unavailability-handling.patch \
   file://0005-Perform-Cynara-runtime-policy-checks-by-default.patch \
   file://0006-Fix-SIGSEGV-on-disconnections.patch \
   file://0007-Switch-from-cynara-to-cynagora.patch \
"

DEPENDS_append_class-target = " cynagora smack"
EXTRA_OECONF_append_class-target = " ${@bb.utils.contains('DISTRO_FEATURES','smack','--enable-cynagora --disable-selinux','',d)}"


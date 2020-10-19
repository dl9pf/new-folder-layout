# Recent ConnMan releases started limiting the capabilities of
# ConnMan. When running on a Smack-enabled system, that change has the
# effect that connmand can no longer change network settings under
# /proc/net because the Smack label of /proc is "_", and connmand
# running with label "System" has no write access to that.
#
# It works when running as normal root with unrestricted capabilities
# because then CAP_MAC_OVERRIDE (a Smack-specific capability) allows
# the process to ignore Smack rules.
#
# We need to ensure that connmand still has that capability.
#
# The alternative would be to set up fine-grained labelling of
# /proc with corresponding rules, which is considerably more work
# and also may depend on kernel changes (like supporting smackfsroot
# for procfs, which seems to be missing at the moment).
#
# Because the solution is to some extend specific to the environment
# in which connmand runs, this change is not submitted upstream
# and it can be overridden by a distro via FIX_CONNMAN_CAPABILITIES.

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append_with-lsm-smack = "\
  file://connman.service.conf \
"

RDEPENDS_${PN}_append_with-lsm-smack = " smack"

FILES_${PN} += "${systemd_unitdir}"

do_install_append_with-lsm-smack() {
  install -Dm0644 ${WORKDIR}/connman.service.conf ${D}${systemd_unitdir}/system/connman.service.d/smack.conf
}

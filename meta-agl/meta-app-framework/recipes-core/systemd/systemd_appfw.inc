FILESEXTRAPATHS_prepend := "${THISDIR}/systemd:"

# Ensures systemd runs with label "System"
EXTRA_OEMESON_append_with-lsm-smack = " -Dsmack-run-label=System"

##################################################################################
# Maintaining trivial, non-upstreamable configuration changes as patches
# is tedious. But in same cases (like early mounting of special directories)
# the configuration has to be in code. We make these changes here directly.
##################################################################################
do_patch[prefuncs] += "patch_systemd"
do_patch[vardeps] += "patch_systemd"
patch_systemd() {
    # Handling of /run and /sys/fs/cgroup. Make /run a transmuting directory to
    # enable systemd communications with services in the User domain.
    # Original patch by Michael Demeter <michael.demeter@intel.com>.
    #
    # We simplify the patching by touching only lines which check the result of
    # mac_smack_use(). Those are the ones which are used when Smack is active.
    #
    # smackfsroot=* on /sys/fs/cgroup may be upstreamable, but smackfstransmute=System::Run
    # is too distro specific (depends on Smack rules) and thus has to remain here.
    sed -i -e 's;\("/sys/fs/cgroup", *"[^"]*", *"[^"]*\)\(.*mac_smack_use.*\);\1,smackfsroot=*\2;' \
           -e 's;\("/run", *"[^"]*", *"[^"]*\)\(.*mac_smack_use.*\);\1,smackfstransmute=System::Run\2;' \
           ${S}/src/core/mount-setup.c
}

##################################################################################
# What follows is temporary.
# This is a solution to the Bug-AGL SPEC-539
# (see https://jira.automotivelinux.org/browse/SPEC-539).
#
# It renames the file "touchscreen.rules" to "55-touchscreen.rules"
# This comes with the recipe systemd_230/234 of poky (meta/recipes-core/systemd)
# It should be removed when poky changes.
##################################################################################
do_install_prepend() {
	mv ${WORKDIR}/touchscreen.rules ${WORKDIR}/55-touchscreen.rules || true
}


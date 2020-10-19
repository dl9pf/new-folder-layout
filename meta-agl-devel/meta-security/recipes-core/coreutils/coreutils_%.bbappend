# Smack patches are included in coreutils v8.22, we just need to enable them.
# The default is not deterministic (enabled if libsmack found), so disable
# explicitly otherwise.
EXTRA_OECONF_SMACK_class-target = "--disable-libsmack"
EXTRA_OECONF_SMACK_with-lsm-smack_class-target = "--enable-libsmack"
EXTRA_OECONF_append_class-target = " ${EXTRA_OECONF_SMACK}"
DEPENDS_append_with-lsm-smack_class-target = " smack"

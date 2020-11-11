require ${@bb.utils.contains('AGL_FEATURES', 'agljailhouse', 'recipes-kernel/linux/linux-jailhouse-5.4.inc', '', d)}


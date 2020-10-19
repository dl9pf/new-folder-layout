FILESEXTRAPATHS_prepend := "${THISDIR}/linux-4.14:"

#-------------------------------------------------------------------------
# smack patches for kernels keys

SRC_URI_append_with-lsm-smack = "\
       file://Smack-Privilege-check-on-key-operations.patch \
       file://Smack-Handle-CGROUP2-in-the-same-way-that-CGROUP.patch \
       "

SRC_URI_append = "\
       file://net-sch_generic-add-if_afp.h-header-to-get-ARPHRD_CA.patch \
       file://net-sch_generic-Use-pfifo_fast-as-fallback-scheduler.patch \
       "

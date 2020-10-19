FILESEXTRAPATHS_prepend := "${THISDIR}/linux-4.19:"

SRC_URI_append = "\
       file://net-sch_generic-Use-pfifo_fast-as-fallback-scheduler.patch \
       "

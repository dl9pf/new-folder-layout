require recipes-kernel/linux/linux-agl.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://cma-256.cfg"

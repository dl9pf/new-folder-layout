# fix for kernel using hardcoded awk while our filesystem only provides gawk
do_install_append() {
    # enforce all scripts to use /usr/bin/awk . This fixes the rpm dependency failure on install of kernel-devsrc
    cd ${D} || true
    ( for i in `grep -srI "\!/bin/awk" | cut -d":" -f1 ` ; do sed -i -e "s#\!/bin/awk#\!/usr/bin/env awk#g" $i ; done ) || true
}
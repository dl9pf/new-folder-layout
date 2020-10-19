do_install_append() {
    # Remove ethernet script deployed by upstream unconditionally (SPEC-3221)
    rm -rf ${D}${systemd_unitdir}/network/80-wired.network || true
}
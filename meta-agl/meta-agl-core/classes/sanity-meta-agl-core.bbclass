addhandler aglcore_bbappend_distrocheck
aglcore_bbappend_distrocheck[eventmask] = "bb.event.SanityCheck"
python aglcore_bbappend_distrocheck() {
    skip_check = e.data.getVar('SKIP_META_AGL_CORE_SANITY_CHECK') == "1"
    if 'aglcore' not in e.data.getVar('AGL_FEATURES').split() and not skip_check:
        bb.warn("You have included the meta-agl-core layer, but \
'aglcore' has not been enabled in your AGL_FEATURES. Some bbappend files \
may not take effect. See the meta-agl-core README for details on enabling \
meta-agl-core support.")
}

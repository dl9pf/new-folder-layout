# Cannot just append to PACKAGECONFIG, as nghttp2's dependencies do not build
# for native/nativesdk, and appending class-target does not work because of
# the weak definition of PACKAGECONFIG in the recipe, so need to copy the
# definition to add nghttp2...
PACKAGECONFIG = "${@bb.utils.filter('DISTRO_FEATURES', 'ipv6', d)} gnutls libidn proxy threaded-resolver verbose zlib nghttp2"

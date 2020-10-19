
# Enabling ptest in image ...

OVERRIDES .= ":agl-ptest"
DISTRO_FEATURES_append = " ptest"
EXTRA_IMAGE_FEATURES_append = " ptest-pkgs"
IMAGE_INSTALL_append = " lua-ptest xmlsec1-ptest libxml2-ptest"

# / ptest

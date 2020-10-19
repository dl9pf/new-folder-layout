# Remove GBM support from Mesa
# TI Platforms provides own GBM library

PACKAGES_remove = "libgbm"
PACKAGES_remove = "libgbm-dev"

EXTRA_OECONF_remove = "--enable-gbm"

DISTRO_FEATURES_append = " AGLCI"
# opencv seems to have a parallel make bug
# ...contrib/modules/xfeatures2d/test/test_features2d.cpp:51:10: fatal error: features2d/test/test_detectors_regression.impl.hpp: No such file or directory
PARALLEL_MAKE_pn-opencv = "-j 1"

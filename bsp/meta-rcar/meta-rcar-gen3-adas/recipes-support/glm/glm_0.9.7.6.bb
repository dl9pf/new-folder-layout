require glm.inc

S = "${WORKDIR}/git"

SRC_URI = " \
    git://github.com/g-truc/${BPN}.git \
    file://0001-Fix-cmake-pathes.patch \
"

# tag: 0.9.7.6
SRCREV = "dff3af56a884e31d21eae7ab3eb72a8d192adc54"

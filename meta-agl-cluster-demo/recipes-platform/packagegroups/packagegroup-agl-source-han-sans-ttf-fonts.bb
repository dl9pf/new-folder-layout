SUMMARY = "The Source Han Sans ttf fonts for AGL"
DESCRIPTION = "A set of packages for source Han Sans ttf fonts"

LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
    packagegroup-agl-source-han-sans-ttf-fonts \
    "

ALLOW_EMPTY_${PN} = "1"


# fonts
RDEPENDS_${PN}_append = " \
    source-han-sans-cn-fonts \
    source-han-sans-jp-fonts \
    source-han-sans-tw-fonts \
"

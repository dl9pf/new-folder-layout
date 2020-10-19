SUMMARY = "Python wrapper for the Fossology API "
DESCRIPTION = "A simple wrapper for the Fossology REST API."
HOMEPAGE = "https://github.com/fossology/fossology-python"
SECTION = "devel"

LICENSE = "MIT"
#LIC_FILES_CHKSUM = "file://LICENSE.md;md5=9f9e04c24f49c23bb097c9d9354b1fcf"

inherit native
#inherit native pypi setuptools3
#inherit pypi setuptools3

#DEPENDS += "python3-requests-native"

SRC_URI[md5sum] = "17b87852a6365fef695e47c5c24092fa"
SRC_URI[sha256sum] = "060d396a2198ca5dab8be49a5e996b5a32d9c4396953adc6a6d322232f3faa8c"

export PYTHON_EXE="${HOSTTOOLS_DIR}/python3"

do_install(){
        pip3 install fossology
}


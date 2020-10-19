SUMMARY     = "QtWayland config file."
DESCRIPTION = "Config file for qtwayland."
SECTION     = "apps"
LICENSE     = "MIT"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://qtwayland"

inherit allarch

QTWAYLAND_DEFAULT_SHELL ?= "xdg-shell"

do_compile[noexec] = "1"

do_install () {
    install -D -m 644 ${WORKDIR}/qtwayland ${D}${sysconfdir}/afm/unit.env.d/qtwayland
    sed -i -e 's/@QT_WAYLAND_DEFAULT_SHELL@/${QTWAYLAND_DEFAULT_SHELL}/' ${D}${sysconfdir}/afm/unit.env.d/qtwayland
}

RPROVIDES_${PN} += "virtual/qtwayland-config"

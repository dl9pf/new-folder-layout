inherit gobject-introspection

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append = "file://org.freedesktop.GeoClue2.Client.conf"

PACKAGECONFIG = "nmea lib"

do_install_append() {
    install -d ${D}/etc/dbus-1/system.d
    install -m 0644 ${WORKDIR}/org.freedesktop.GeoClue2.Client.conf ${D}/etc/dbus-1/system.d/org.freedesktop.GeoClue2.Client.conf
}

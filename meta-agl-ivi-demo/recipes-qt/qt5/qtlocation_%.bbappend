FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
    file://0001-mapbox-update-API-url-to-match-new-schema.patch \
    "

# Need to explicitly enable the various plugins
PACKAGECONFIG += " \
	geoservices_osm \
	geoservices_here \
	geoservices_itemsoverlay \
	geoservices_mapbox \
	geoservices_mapboxgl \
"

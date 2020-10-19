
cat <<EOF >> ${BUILDDIR}/conf/bblayers.conf

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
# fragment {
# ${METADIR}/meta-agl/templates/feature/agl-sota/50_setup.sh
#
EOF

case ${MACHINE} in
	"qemux86-64")
	  echo "BBLAYERS =+ \"\${METADIR}/external/meta-updater-qemux86-64\"" >> ${BUILDDIR}/conf/bblayers.conf;;
	"raspberrypi3" | "raspberrypi4")
	  echo "BBLAYERS =+ \"\${METADIR}/external/meta-updater-raspberrypi\"" >> ${BUILDDIR}/conf/bblayers.conf;;
	*)
	  echo "#No extra SOTA feature layer for MACHINE ${MACHINE}" >> ${BUILDDIR}/conf/bblayers.conf;;
esac


cat <<EOF >> ${BUILDDIR}/conf/bblayers.conf

#
# }
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

EOF

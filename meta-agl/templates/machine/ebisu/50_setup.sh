# setup proprietary gfx drivers and multimedia packages
pushd $METADIR 2>/dev/null

COPY_SCRIPT="$METADIR/bsp/meta-renesas/meta-rcar-gen3/docs/sample/copyscript/copy_proprietary_softwares.sh"
EXTRACT_DIR=$METADIR/binary-tmp
#EBISU_BIN_PATH should contain the path where the .zip archive of E3 binaries is.
#CUSTOM_RENESAS_CONFIG_SCRIPT should contain the custom script needed for setup. If not filled, do not failed, just warn.

# Check the ebisu binaries path
if [[ ! -d $EBISU_BIN_PATH ]] || [[ $EBISU_BIN_PATH == "" ]]; then
	echo "ERROR: E3 Binary path not valid."
	echo "HELP: Export the path where the E3 Binaries ZIP file is into 'EBISU_BIN_PATH' then launch the setup again."
	echo "HELP: Example: '$ export EBISU_BIN_PATH=`pwd`/ebisu_binaries'"
	exit 1
else
	[ -z "$(ls -A $EBISU_BIN_PATH)" ] && echo "ERROR: $EBISU_BIN_PATH is empty. Add the E3 Binaries ZIP file inside and try again." && exit 1
fi

if [ -f $COPY_SCRIPT ]; then
	# Extract the ZIP into the tmp directory
	mkdir -p $EXTRACT_DIR
	for PROPRIETARY_BIN in `ls -1 $EBISU_BIN_PATH/*.zip`
	do
		unzip -q -o $PROPRIETARY_BIN -d $EXTRACT_DIR
	done

	cd $METADIR/bsp/meta-renesas/
	$COPY_SCRIPT $EXTRACT_DIR
	cd ..

	# Clean temp dir
	rm -r $EXTRACT_DIR
else
	echo "ERROR: Script to copy Renesas proprietary drivers for $MACHINE not found. No additionnal setup to do."
	exit 1
fi

if [[ ! -z $CUSTOM_RENESAS_CONFIG_SCRIPT ]] && [[ -f $CUSTOM_RENESAS_CONFIG_SCRIPT ]]; then
	echo "Launching Renesas custom setup script ($CUSTOM_RENESAS_CONFIG_SCRIPT)..."
	$CUSTOM_RENESAS_CONFIG_SCRIPT
else
	echo "WARNING: Renesas custom setup script for $MACHINE not found."
fi

popd 2>/dev/null

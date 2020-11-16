#!/bin/sh

MOUNT_OPTIONS_DEFAULT="ro,noexec"
MOUNT_OPTIONS_VFAT="umask=0022"
MOUNT_OPTIONS_EXT=""
MOUNT_OPTIONS_NTFS=""
MOUNT_OPTIONS_ISO9660=""

VERBOSE=false

# Source a configuration file that can override mount options if exists
[ -f /etc/automount.conf ] && . /etc/automount.conf

mount_device() {
	MOUNT_OPTIONS=""
	FSTYPE="$( udevadm info "${1}" "${2}" | awk -v FS== '/ID_FS_TYPE/ {print $2}' )"
	DEVNAME="$( udevadm info "${1}" "${2}" | awk -v FS== '/DEVNAME/ {print $2}' )"
	case $FSTYPE in
		vfat)
			MOUNT_OPTIONS="${MOUNT_OPTIONS_VFAT}"
			;;
		ext[2-4])
			MOUNT_OPTIONS="${MOUNT_OPTIONS_EXT}"
			;;
		ntfs)
			MOUNT_OPTIONS="${MOUNT_OPTIONS_NTFS}"
			;;
		iso9660)
			MOUNT_OPTIONS="${MOUNT_OPTIONS_ISO9660}"
			;;
		"")
			if $VERBOSE; then
				echo "[INFO][${DEVNAME}] Not a partition with a filesystem!"
			fi
			return
			;;
		*)
			echo "[WARNING][${DEVNAME}] The filesystem '${FSTYPE}' is not supported!"
			return
			;;
	esac

	if [ -n "${MOUNT_OPTIONS_DEFAULT}" ]; then
		if [ -z "${MOUNT_OPTIONS}" ]; then
			MOUNT_OPTIONS="${MOUNT_OPTIONS_DEFAULT}"
		else
			MOUNT_OPTIONS="${MOUNT_OPTIONS_DEFAULT},${MOUNT_OPTIONS}"
		fi
	fi
	if $VERBOSE; then
		echo "[INFO][${DEVNAME}] Mounting a ${FSTYPE}'s filesystem with options: ${MOUNT_OPTIONS}"
	fi

	if command -v udisksctl > /dev/null 2>&1; then
		if [ -n "${MOUNT_OPTIONS}" ]; then
			MOUNT_OPTIONS="-o ${MOUNT_OPTIONS}"
		fi
		udisksctl mount -t "${FSTYPE}" -b "${DEVNAME}" ${MOUNT_OPTIONS}
	elif command -v udisks >/dev/null 2>&1; then
		if [ -n "${MOUNT_OPTIONS}" ]; then
			MOUNT_OPTIONS="--mount-options ${MOUNT_OPTIONS}"
		fi
		udisks --mount-fstype "${FSTYPE}" --mount "${DEVNAME}" ${MOUNT_OPTIONS}
	else
		echo "[ERROR] Unable to find binary for mounting ${DEVNAME}" >&2
		return
	fi
	if [ "$?" -ne "0" ]; then
		echo "[ERROR] Failed to mount the device ${DEVNAME} of type ${FSTYPE} with options ${MOUNT_OPTIONS}" >&2
	fi
}

# At startup, remove empty directories that may exists
rmdir /media/* > /dev/null 2>&1

# Mount already plugged devices
for DEVICE in $( lsblk -dn | cut -d' ' -f1 ); do
	REMOVABLE=$( cat "/sys/block/${DEVICE}/removable" )
	if [ "${REMOVABLE}" -eq "1" ]; then
		for PART in "/dev/${DEVICE}"*; do
			mount_device -n "${PART}"
		done
	fi
done

# Wait for plug events and mount devices
stdbuf -oL -- udevadm monitor --udev -s block |
while read -r -- _ _ EVENT DEVPATH _
do
	if [ "${EVENT}" = "add" ]; then
		mount_device -p "/sys/${DEVPATH}"
	fi
done

find_and_ack_eula() {
    # Handle EULA , if needed. This is a generic method to handle BSPs
    # that might (or not) come with a EULA. If a machine has a EULA, we
    # assume that its corresponding layers has conf/EULA/$MACHINE file
    # with the EULA text, which we will display to the user and request
    # for acceptance. If accepted, the variable ACCEPT_EULA_$MACHINE is
    # set to 1 in local.conf, which can later be used by the BSP.
    # If the env variable EULA_$MACHINE is set it is used by default,
    # without prompting the user.
    # FIXME: there is a potential issue if the same $MACHINE is set in more than one layer.. but we should assert that earlier
    # $1 is layer directory
    # $2 is location of EULA file relative to layer directory
    if test x"" == x"$2"; then
        EULA=$(find $1 -print | grep "conf/eula/$MACHINE" | grep -v scripts | grep -v openembedded-core || true)
    else
	EULA=$1/$2
    fi
    if [ -n "$EULA" ]; then
        # remove '-' since we are constructing a bash variable name here
        EULA_MACHINE="EULA_$(echo $MACHINE | sed 's/-//g')"
        # NOTE: indirect reference / dynamic variable
        if [ -n "${!EULA_MACHINE}" ]; then
            # the EULA_$MACHINE variable is set in the environment, so we just configure
            # ACCEPT_EULA_$MACHINE in local.conf
            EULA_ACCEPT=${!EULA_MACHINE}
        else
            # so we need to ask user if he/she accepts the EULA:
            cat <<EOF
The BSP for $MACHINE depends on packages and firmware which are covered by an 
End User License Agreement (EULA). To have the right to use these binaries
in your images, you need to read and accept the following...

The firmware package can contains several types of firmware (depending on BSP):

* bootloaders: the first stage bootloaders are proprietary for this
  board, they are included in this firmware package.
* firmware for the power management 'companion' core: on QCOM SoC some
  power management features are implemented in a companion core , called
  RPM, and not on the main CPU.
* firmware for GPU, WLAN, DSP/GPS and video codecs. These firmware are
  used by their respective linux drivers (DRM, wlan, v4l2, .. ) and are
  loaded on-demand by the main CPU onto the various cores on the SoC.
EOF

            echo
            REPLY=
            while [ -z "$REPLY" ]; do
                echo -n "Do you want to read the EULA ? (y/n) "
                read REPLY
                case "$REPLY" in
                    y|Y)
                        READ_EULA=1
                        ;;
                    n|N)
                        READ_EULA=0
                        ;;
                    *)
                        REPLY=
                        ;;
                esac
            done

            if [ "$READ_EULA" == 1 ]; then
                more -d ${EULA}
                echo
                REPLY=
                while [ -z "$REPLY" ]; do
                    echo -n "Do you accept the EULA you just read? (y/n) "
                    read REPLY
                    case "$REPLY" in
                        y|Y)
                            echo "EULA has been accepted."
                            EULA_ACCEPT=1
                            ;;
                        n|N)
                            echo "EULA has not been accepted."
                            ;;
                        *)
                            REPLY=
                            ;;
                    esac
                done
            fi
        fi
    fi
}

EULA_ACCEPT=0

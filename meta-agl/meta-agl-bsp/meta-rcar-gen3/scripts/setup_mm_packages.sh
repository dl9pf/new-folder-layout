#!/bin/bash

ZIP_1="R-Car_Gen3_Series_Evaluation_Software_Package_for_Linux-weston8-20191206.zip"
ZIP_2="R-Car_Gen3_Series_Evaluation_Software_Package_of_Linux_Drivers-weston8-20191021.zip"

COPY_SCRIPT="$METADIR/bsp/meta-renesas/meta-rcar-gen3/docs/sample/copyscript/copy_evaproprietary_softwares.sh"

test -f ${XDG_CONFIG_HOME:-~/.config}/user-dirs.dirs && source ${XDG_CONFIG_HOME:-~/.config}/user-dirs.dirs
DOWNLOAD_DIR=${XDG_DOWNLOAD_DIR:-$HOME/Downloads}
EXTRACT_DIR=$METADIR/binary-tmp

stdout_in_terminal=1
[[ -t 1 ]] && stdout_in_terminal=1
function color {
    [[ $stdout_in_terminal == 0 ]] && return
    for k in $*; do
        case $k in
            bold) tput bold;;
            none) tput sgr0;;
            *) tput setaf $k;;
        esac
        if [[ $? != 0 ]]; then
            echo "tput: terminal doesn't support color settings, continuing" >&2
            true
        fi
    done
}
color_green=$(color bold 2)
color_yellow=$(color bold 3)
color_red=$(color bold 1)
color_none=$(color none)

function error() {
    echo "${color_red}$@${color_none}" >&2
}

function log() {
    echo "$@" >&2
}

function copy_mm_packages() {
    # first clean up workdir on need
    if [ -d $EXTRACT_DIR ]; then
        if [ -f $EXTRACT_DIR/$ZIP_1 -a -f $EXTRACT_DIR/$ZIP_2 ]; then
            log   "The graphics and multimedia acceleration packages for R-Car Gen3 look already installed."
            log   "To force their reinstallation, please, remove manually the directory:"
            log   "           $EXTRACT_DIR"
            log
            return 0
        fi
        rm -r $EXTRACT_DIR
    fi

    if [ -f $DOWNLOAD_DIR/$ZIP_1 -a -f $DOWNLOAD_DIR/$ZIP_2 ]; then
        mkdir -p $EXTRACT_DIR
        cp --update $DOWNLOAD_DIR/$ZIP_1 $EXTRACT_DIR
        cp --update $DOWNLOAD_DIR/$ZIP_2 $EXTRACT_DIR
    else
        error "ERROR: FILES \""+$DOWNLOAD_DIR/$ZIP_1+"\" NOT EXTRACTING CORRECTLY"
        error "ERROR: FILES \""+$DOWNLOAD_DIR/$ZIP_2+"\" NOT EXTRACTING CORRECTLY"
        log   "The graphics and multimedia acceleration packages for "
        log   "the R-Car Gen3 board BSP can be downloaded from:"
        log   "<https://www.renesas.com/us/en/solutions/automotive/rcar-download/rcar-demoboard-2.html>"
        log
        error  "These 2 files from there should be stored in your"
        error  "'$DOWNLOAD_DIR' directory."
        error  "  $ZIP_1"
        error  "  $ZIP_2"
        return 1
    fi

    if [ -f $COPY_SCRIPT ]; then
        cd $METADIR/bsp/meta-renesas/
        $COPY_SCRIPT -d -f $EXTRACT_DIR
        cd ..
    else
        log   "scripts to copy drivers for Gen3 not found."
        return 1
    fi
}

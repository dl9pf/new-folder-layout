#!/bin/bash

################################################################################
#
# The MIT License (MIT)
#
# Copyright (c) 2016-2019 Stéphane Desneux <sdx@iot.bzh>
#           (c) 2016 Jan-Simon Möller <jsmoeller@linuxfoundation.org>
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#
################################################################################

# this script shouldn't be called directly, but through aglsetup.sh that will in
# turn execute (source) generated instructions back in the parent shell,
# whether it's bash, zsh, or any other supported shell

VERSION=1.2.0
DEFAULT_MACHINE=qemux86-64
DEFAULT_BUILDDIR=./build
VERBOSE=0
SHOWVERSION=0
: ${DEBUG:=false}

#SCRIPT=$(basename $BASH_SOURCE)
SCRIPT=aglsetup.sh
SCRIPTDIR=$(cd $(dirname $BASH_SOURCE) && pwd -P)
METADIR=$(cd $(dirname $BASH_SOURCE)/../.. && pwd -P)

function info() { echo "$@" >&2; }
function infon() { echo -n "$@" >&2; }
function error() { echo "ERROR: $@" >&2; return 1; }
function verbose() { [[ $VERBOSE == 1 ]] && echo "$@" >&2; return 0; }
function debug() { $DEBUG && echo "DEBUG: $@" >&2; return 0;}

debug "------------ $SCRIPT: starting with command line arguments: $@"

#compute AGL_REPOSITORIES
AGL_REPOSITORIES=$(for x in $(ls -d $METADIR/meta-*/templates/{machine,feature} $METADIR/bsp/*/templates/machine 2>/dev/null); do echo $(basename $(dirname $(dirname $x))); done | sort -u)

function list_machines() {
	for a in $@; do
		for y in $(ls -d $METADIR/{.,bsp}/$a/templates/machine/* 2>/dev/null); do
			echo $(basename $y)
		done
	done
}

function list_all_machines() {
	for x in $AGL_REPOSITORIES; do
		list_machines $x
	done
}

function validate_builddir() {
	if [[ "$BUILDDIR" =~ [[:space:]] ]]; then
		error "Build dir '$BUILDDIR' shouldn't contain any space"
	fi
	debug "Build dir is valid"
}

function validate_machines() {
	list_all_machines | sort | uniq -c | while read cnt machine; do
		[[ $cnt == 1 ]] && continue
		info "Machine $machine found in the following repositories:"
		for x in $(ls -d $METADIR/*/templates/machine/$machine $METADIR/bsp/*/templates/machine/$machine 2>/dev/null); do
			info "   - $x"
		done
		error "Multiple machine templates are not allowed"
	done
	debug "Machines list has no duplicate."
}

function list_features() {
	for x in $@; do
		for y in $(ls -d $METADIR/$x/templates/feature/* 2>/dev/null); do
			echo $(basename $y)
		done
	done
}

function list_all_features() {
	for x in $AGL_REPOSITORIES; do
		list_features $x
	done
}

function validate_features() {
	list_all_features | sort | uniq -c | while read cnt feature; do
		[[ $cnt == 1 ]] && continue;
		info "Feature $feature found in the following repositories:"
		for x in $(ls -d $METADIR/*/templates/feature/$feature 2>/dev/null); do
			info "   - $x"
		done
		error "Multiple feature templates are not allowed"
	done
	debug "Features list has no duplicate."
}

function find_machine_dir() {
	machine=$1
	for x in $AGL_REPOSITORIES; do
		dirs=$(ls -d $METADIR/{.,bsp}/$x/templates/machine/$machine 2>/dev/null)
		for dir in $dirs; do
		    [[ -d $dir ]] && { echo $dir; return 0; }
		done
	done
	return 1
}

function find_feature_dir() {
	feature=$1
	for x in $AGL_REPOSITORIES; do
		dir=$METADIR/$x/templates/feature/$feature
		[[ -d $dir ]] && { echo $dir; return 0; }
	done
	return 1
}

function usage() {
    cat <<EOF >&2
Usage: . $SCRIPT [options] [feature [feature [... ]]]

Version: $VERSION
Compatibility: bash, zsh, ksh

Options:
   -m|--machine <machine>
      what machine to use
      default: '$DEFAULT_MACHINE'
   -b|--build <directory>
      build directory to use
      default: '$DEFAULT_BUILDDIR'
   -s|--script <filename>
      file where setup script is generated
      default: none (no script)
   -f|--force
      flag to force overwriting any existing configuration
      default: false
   -r|--rpm-revision <schema>
      Specify how to handle RPM packages revisions
      <schema> can be:
          'prservice[:<address>]' : Use a PR service daemon.
              if <address> is not specified, the default value 'localhost:0'
              is used (shortcut for a PR service started by bitbake)
          'timestamp' : Use a generated time stamp (UTC).
          'value:<revision>' : Use <revision> explicitly.
          'none' : Do nothing.
   -t|--topic <value>
      Specify an optional topic for this setup.
      If specified, the topic will be propagated in build manifests:
         - in deployment dir: tmp/deploy/images/*/build-info
         - in target image: /etc/platform-info/build
         - in SDK: tmp/deploy/sdk/*.build-info
   -v|--verbose
      verbose mode
      default: false
   -V|--version
      display version, set AGLSETUP_VERSION variable with version value and exit
   -d|--debug
      debug mode
      for early debug, set env variable DEBUG. 
      for example:
           DEBUG=true source aglsetup.sh -V
      default: false
   -h|--help
      get some help

EOF
	local buf

	echo "Available machines:" >&2
	for x in $AGL_REPOSITORIES; do
		buf=$(list_machines $x)
		[[ -z "$buf" ]] && continue
		echo "   [$x]"
		for y in $buf; do
			[[ $y == $DEFAULT_MACHINE ]] && def="* " || def="  "
			echo "     $def$y"
		done
	done
	echo >&2

	echo "Available features:" >&2
	for x in $AGL_REPOSITORIES; do
		buf=$(list_features $x)
		[[ -z "$buf" ]] && continue
		echo "   [$x]"
		for feature in $buf; do
			print_feature="$feature"
			featuredir=$(find_feature_dir $feature)
			if [ -e $featuredir/included.dep ];then
				print_feature="$print_feature :($(find_feature_dependency $feature $feature))"
			fi;
			echo "       $print_feature"
		done
	done
	echo >&2
}

function append_fragment() {
	basefile=$1; shift # output file
	f=$1; shift # input file
	label=$(echo "$@")

	debug "adding fragment to $basefile: $f"
	echo >>$basefile
	echo "# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #" >>$basefile
	echo "# fragment { " >>$basefile
	[[ -f $f ]] && echo "# $f" >>$basefile || true
	echo "#" >>$basefile
	[[ -n "$label" ]] && echo "$label" >>$basefile
	[[ -f $f ]] && cat $f >>$basefile || true
	echo "" >>$basefile
	echo "#" >>$basefile
	echo "# }" >>$basefile
	echo "# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #" >>$basefile
	[[ -f $f ]] && echo $f >>$BUILDDIR/conf/fragments.log || true
}

function execute_setup() {
	script=$1
	debug "Executing script $script"
	opts="-e"
	$DEBUG && opts="$opts -x"
	pushd $BUILDDIR &>/dev/null
		$BASH $opts $script \
			&& rc=0 \
			|| { rc=$?; error "Script $script failed"; }
	popd &>/dev/null
	return $rc
}


# process all fragments
FRAGMENTS_BBLAYERS=""
FRAGMENTS_LOCALCONF=""
FRAGMENTS_SETUP=""
function process_fragments() {
	for dir in "$@"; do
		debug "processing fragments in dir $dir"

		verbose "   Searching fragments: $dir"

		# lookup for files with priorities specified: something like xx_bblayers.conf.yyyyy.inc
		for x in $(ls $dir/??[._]bblayers.conf*.inc 2>/dev/null); do
			FRAGMENTS_BBLAYERS="$FRAGMENTS_BBLAYERS $(basename $x):$x"
			verbose "      priority $(basename $x | cut -c1-2): $(basename $x)"
		done

		# same for local.conf
		for x in $(ls $dir/??[._]local.conf*.inc 2>/dev/null); do
			FRAGMENTS_LOCALCONF="$FRAGMENTS_LOCALCONF $(basename $x):$x"
			verbose "      priority $(basename $x | cut -c1-2): $(basename $x)"
		done

		# same fot setup.sh
		for x in $(ls $dir/??[._]setup*.sh 2>/dev/null); do
			FRAGMENTS_SETUP="$FRAGMENTS_SETUP $(basename $x):$x"
			verbose "      priority $(basename $x | cut -c1-2): $(basename $x)"
		done
	done
}

function containsFeature () {
  for feature in $1; do
    [[ "$feature" == "$2" ]] && return 1;
  done;
  return 0;
}

function find_feature_dependency() {
	res_dep_features=""
	featuredir=$(find_feature_dir $1)
	full_feature=$2;
	if [ -e $featuredir/included.dep ]; then
		dep_features="$(cat $featuredir/included.dep)"
		for dep_feature in $dep_features; do
			full_feature="$full_feature $res_dep_features"
			res_dep_features="$res_dep_features $dep_feature"
			if  containsFeature $dep_feature $full_feature ; then
				res_dep_features="$res_dep_features $(find_feature_dependency $dep_feature $full_feature)"
			fi;
		done;
	fi;
	echo "$res_dep_features";
	return 0;
}

GLOBAL_ARGS=( "$@" )
debug "Parsing arguments: $@"
TEMP=$(getopt -o m:b:r:t:s:fvVdh --long machine:,builddir:,rpm-revision:,topic:,script:,force,verbose,version,debug,help -n $SCRIPT -- "$@")
[[ $? != 0 ]] && { usage; exit 1; }
eval set -- "$TEMP"

set -e

### default options values
MACHINE=$DEFAULT_MACHINE
BUILDDIR=$DEFAULT_BUILDDIR
SETUPSCRIPT=
FORCE=
RPMREVISION=
TOPIC=
SETUP_MANIFEST=aglsetup.manifest

while true; do
	case "$1" in
		-m|--machine)      MACHINE=$2; shift 2;;
		-b|--builddir)     BUILDDIR=$2; shift 2;;
		-s|--setupscript)  SETUPSCRIPT=$2; shift 2;;
		-f|--force)        FORCE=1; shift;;
		-r|--rpm-revision) RPMREVISION=$2; shift 2;;
		-t|--topic)        TOPIC=$2; shift 2;;
		-v|--verbose)      VERBOSE=1; shift;;
		-V|--version)      SHOWVERSION=1; shift;;
		-d|--debug)        VERBOSE=1; DEBUG=true; shift;;
		-h|--help)         HELP=1; shift;;
		--)                shift; break;;
		*) error "Arguments parsing error"; exit 1;;
	esac
done

[[ "$HELP" == 1 ]] && { usage; exit 0; }

if [[ "$SHOWVERSION" == 1 ]]; then
	# display version on stdout
	echo "$VERSION"

	# generate output script if requested by caller
	if [[ -n "$SETUPSCRIPT" ]]; then
		cat <<EOF >$SETUPSCRIPT
AGLSETUP_VERSION=$VERSION
EOF
	fi

	# IMPORTANT: exit successfully
	# older aglsetup scripts with version <1.2.0 will fail with option --version
	exit 0
fi

info "------------ $SCRIPT: Starting"

verbose "Command line arguments: ${GLOBAL_ARGS[@]}"

# the remaining args are the features
FEATURES="$@"

# validate the machine list
debug "validating machines list"
validate_machines

# validate the machine
debug "validating machine $MACHINE"
find_machine_dir $MACHINE >/dev/null || error "Machine '$MACHINE' not found in [ $(list_all_machines)]"

# validate the features list
debug "validating features list"
validate_features

TMP_FEATURES="";
for FEATURE in $FEATURES;do
    TMP_FEATURES="$TMP_FEATURES $FEATURE"
    TMP_FEATURES="$TMP_FEATURES $(find_feature_dependency $FEATURE $TMP_FEATURES)"
done
# remove duplicate features if any
FEATURES=$(for x in $TMP_FEATURES; do echo $x; done | sort -u | awk '{printf("%s ",$1);}')

# validate the features
for f in $FEATURES; do
	debug "validating feature $f"
	find_feature_dir $f >/dev/null || error "Feature '$f' not found in [ $(list_all_features)]"
done

# validate build dir
debug "validating builddir $BUILDDIR"
BUILDDIR=$(mkdir -p "$BUILDDIR" && cd "$BUILDDIR" && pwd -P)
validate_builddir

###########################################################################################
function dump_log() {
	info "    ------------ $(basename $1) -----------------"
	sed 's/^/   | /g' $1
	info "    ----------------------------------------"
}

function genconfig() {
	info "Generating configuration files:"
	info "   Build dir: $BUILDDIR"
	info "   Machine: $MACHINE"
	info "   Features: $FEATURES"

	# step 1: run usual OE setup to generate conf dir
	export TEMPLATECONF=$(cd $SCRIPTDIR/../templates/base && pwd -P)
	debug "running oe-init-build-env with TEMPLATECONF=$TEMPLATECONF"
	info "   Running $METADIR/external/poky/oe-init-build-env"
	info "   Templates dir: $TEMPLATECONF"

	CURDIR=$(pwd -P)
	. $METADIR/external/poky/oe-init-build-env $BUILDDIR >/dev/null
	cd $CURDIR

	# step 2: concatenate other remaining fragments coming from base
	process_fragments $TEMPLATECONF

	# step 3: fragments for machine
	process_fragments $(find_machine_dir $MACHINE)

	# step 4: fragments for features
	for feature in $FEATURES; do
		process_fragments $(find_feature_dir $feature)
	done

	# step 5: sort fragments and append them in destination files
	FRAGMENTS_BBLAYERS=$(sed 's/ /\n/g' <<<$FRAGMENTS_BBLAYERS | sort)
	debug "bblayer fragments: $FRAGMENTS_BBLAYERS"
	info "   Config: $BUILDDIR/conf/bblayers.conf"
	for x in $FRAGMENTS_BBLAYERS; do
		file=${x/#*:/}
		append_fragment $BUILDDIR/conf/bblayers.conf $file
		verbose "      + $file"
	done

	FRAGMENTS_LOCALCONF=$(sed 's/ /\n/g' <<<$FRAGMENTS_LOCALCONF | sort)
	debug "localconf fragments: $FRAGMENTS_LOCALCONF"
	info "   Config: $BUILDDIR/conf/local.conf"
	for x in $FRAGMENTS_LOCALCONF; do
		file=${x/#*:/}
		append_fragment $BUILDDIR/conf/local.conf $file
		verbose "      + $file"
	done
	# special fragment to call distro-manifest-generator.sh from 
	# meta-agl-profile-core/recipes-core/distro-build-manifest/distro-build-manifest.bb
	append_fragment $BUILDDIR/conf/local.conf /dev/stdin "# generated by $(realpath $BASH_SOURCE)" <<-EOF
		DISTRO_SETUP_MANIFEST = "$(realpath -Ls $BUILDDIR)/$SETUP_MANIFEST"
		DISTRO_MANIFEST_GENERATOR = "$(dirname $(realpath $BASH_SOURCE))/distro-manifest-generator.sh"
	EOF

	FRAGMENTS_SETUP=$(sed 's/ /\n/g' <<<$FRAGMENTS_SETUP | sort)
	debug "setup fragments: $FRAGMENTS_SETUP"
	cat <<EOF >$BUILDDIR/conf/setup.sh
#!/bin/bash

# this script has been generated by $BASH_SOURCE

export MACHINE="$MACHINE"
export FEATURES="$FEATURES"
export BUILDDIR="$BUILDDIR"
export METADIR="$METADIR"
export RPMREVISION="$RPMREVISION"
export LOCALCONF="$BUILDDIR/conf/local.conf"

echo "--- beginning of setup script"
EOF
	info "   Setup script: $BUILDDIR/conf/setup.sh"
	for x in $FRAGMENTS_SETUP; do
		file=${x/#*:/}
		append_fragment $BUILDDIR/conf/setup.sh $file "echo '--- fragment $file'"
		verbose "      + $file"
	done
	append_fragment $BUILDDIR/conf/setup.sh "" "echo '--- end of setup script'"

	infon "   Executing setup script ... "
	execute_setup $BUILDDIR/conf/setup.sh 2>&1 | tee $BUILDDIR/conf/setup.log
	[[ ${PIPESTATUS[0]} == 0 ]] && {
		info "OK"
		[[ $VERBOSE == 1 ]] && dump_log $BUILDDIR/conf/setup.log
		rm $BUILDDIR/conf/setup.sh
	} \
	|| {
		info "FAIL: please check $BUILDDIR/conf/setup.log"
		dump_log $BUILDDIR/conf/setup.log
		return 1
	}
}

###########################################################################################

# check for overwrite
[[ $FORCE -eq 1 ]] && rm -f \
	$BUILDDIR/conf/local.conf \
	$BUILDDIR/conf/bblayers.conf \
	$BUILDDIR/conf/setup.* \
	$BUILDDIR/conf/*.log

####### step 1: generate configuration file #######

if [[ -f $BUILDDIR/conf/local.conf || -f $BUILDDIR/conf/bblayers.conf ]]; then
	info "Configuration files already exist:"
	for x in $BUILDDIR/conf/local.conf $BUILDDIR/conf/bblayers.conf; do
		[[ -f $x ]] && info "   - $x"
	done
	info "Skipping configuration files generation."
	info "Use option -f|--force to overwrite existing configuration."
else
	genconfig
fi

####### step 2: generate aglsetup.manifest #######

infon "Generating setup manifest: $BUILDDIR/$SETUP_MANIFEST ... "
for x in /etc/os-release /usr/lib/os-release; do
	[[ -f $x ]] && . $x
done
FEATURES_md5=$(echo $FEATURES|md5sum -|awk '{print $1;}')
cat <<EOF >$BUILDDIR/$SETUP_MANIFEST
# ----------------------------------------------
# This fragment has been generated by $SCRIPT at setup time

# distro name
DIST_DISTRO_NAME="AGL"

# target machine as passed to $SCRIPT
DIST_MACHINE="$MACHINE"

# features as resolved by $SCRIPT
DIST_FEATURES="$FEATURES"
DIST_FEATURES_MD5="${FEATURES_md5}"

# build host information deduced from os-release
DIST_BUILD_HOST="$(id -un)@$(hostname -f || hostname || hostname -s)"
DIST_BUILD_OS="${PRETTY_NAME:-${NAME} ${VERSION} [COMPUTED]}"

# meta directory
DIST_METADIR="$METADIR"

# timestamp
DIST_SETUP_TS="$(date -u +%Y%m%d_%H%M%S_%Z)"

# topic
DIST_SETUP_TOPIC="$TOPIC"

# ------------ end of $SCRIPT fragment --------
EOF
info "OK"

####### step 3: generate agl-init-build-env #######

# always generate setup script in builddir: it can be sourced later manually without re-running the setup
infon "Generating setup file: $BUILDDIR/agl-init-build-env ... "

cat <<EOF >$BUILDDIR/agl-init-build-env
. $METADIR/external/poky/oe-init-build-env $BUILDDIR
if [ -n "\$DL_DIR" ]; then
	BB_ENV_EXTRAWHITE="\$BB_ENV_EXTRAWHITE DL_DIR"
fi
if [ -n "\$SSTATE_DIR" ]; then
	BB_ENV_EXTRAWHITE="\$BB_ENV_EXTRAWHITE SSTATE_DIR"
fi
export BB_ENV_EXTRAWHITE
unset TEMPLATECONF
EOF
info "OK"

####### step 4: generate output script #######

# finally, generate output script if requested by caller
if [[ -n "$SETUPSCRIPT" ]]; then
	debug "generating setupscript in $SETUPSCRIPT"
	cat <<EOF >$SETUPSCRIPT
. $BUILDDIR/agl-init-build-env
EOF
fi

info "------------ $SCRIPT: Done"

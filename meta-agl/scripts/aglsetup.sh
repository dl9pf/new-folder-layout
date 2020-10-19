################################################################################
#
# The MIT License (MIT)
#
# Copyright (c) 2016 Stéphane Desneux <sdx@iot.bzh>
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

# detect if this script is sourced: see http://stackoverflow.com/a/38128348/6255594
SOURCED=0
if [ -n "$ZSH_EVAL_CONTEXT" ]; then
	[[ $ZSH_EVAL_CONTEXT =~ :file$ ]] && { SOURCED=1; SOURCEDIR=$(cd $(dirname -- $0) && pwd -P); }
elif [ -n "$KSH_VERSION" ]; then
	[[ "$(cd $(dirname -- $0) && pwd -P)/$(basename -- $0)" != "$(cd $(dirname -- ${.sh.file}) && pwd -P)/$(basename -- ${.sh.file})" ]] && { SOURCED=1; SOURCEDIR=$(cd $(dirname -- ${.sh.file}) && pwd -P); }
elif [ -n "$BASH_VERSION" ]; then
	[[ $0 != "$BASH_SOURCE" ]] && { SOURCED=1; SOURCEDIR=$(cd $(dirname -- $BASH_SOURCE) && pwd -P); }
fi

if [ $SOURCED -ne 1 ]; then
	unset SOURCED
	unset SOURCEDIR
	echo "Error: this script needs to be sourced in a supported shell" >&2
	echo "Please check that the current shell is bash, zsh or ksh and run this script as '. $0 <args>'" >&2
	exit 56
else
	unset SOURCED
	tmpfile=$(mktemp /tmp/aglsetup.XXXXXXXX)
	$SOURCEDIR/.aglsetup_genconfig.bash -s $tmpfile "$@"
	rc=$?
	unset SOURCEDIR
	if [ $rc -eq 0 ]; then
		source $tmpfile
		rc=$?
	else
		echo "Error: configuration files generation failed. Environment is not ready."
	fi

	rm -f $tmpfile
	unset tmpfile
	return $rc
fi


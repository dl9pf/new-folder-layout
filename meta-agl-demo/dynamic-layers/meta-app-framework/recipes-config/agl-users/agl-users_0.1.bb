inherit allarch useradd

SUMMARY = "AGL Users Seed"
DESCRIPTION = "This is a core framework component that\
 defines how users are managed and who are the default users."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

ALLOW_EMPTY_${PN} = "1"

USERADD_PACKAGES = "${PN}"

USERADDEXTENSION = "useradd-staticids"

GROUPADD_PARAM_${PN} = "\
	--system display ; \
	--system weston-launch ; \
	-g 1001 agl-driver ; \
	-g 1002 agl-passenger \
"

USERADD_PARAM_${PN} = "\
  -g 1001 -u 1001 -o -d /home/1001 -m -K PASS_MAX_DAYS=-1 agl-driver ; \
  -g 1002 -u 1002 -o -d /home/1002 -m -K PASS_MAX_DAYS=-1 agl-passenger ; \
  --gid display --groups weston-launch,video,input --home-dir /run/platform/display --shell /bin/false --comment \"Display daemon\" --key PASS_MAX_DAYS=-1 display \
"

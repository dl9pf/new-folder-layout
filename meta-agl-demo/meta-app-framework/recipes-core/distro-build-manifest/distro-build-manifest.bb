SUMMARY = "Distribution build manifest"
DESCRIPTION = "The folder /etc/platform-info contains build manifest (SPEC-720)."
LICENSE = "MIT"

# information distributed by the package is machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

# dependencies of ${DISTRO_MANIFEST_GENERATOR}
DEPENDS = "coreutils-native bash-native git-native gawk-native sed-native jq-native"

# force a rebuild everytime a build is started
do_compile[nostamp] = "1"

# borrowed to os-release.bb (output format is very close)
python do_compilestep1 () {
   import shutil
   with open(d.expand('${B}/bbinfo-deploy'),'w') as f:
      allkeys=[]
      for field in d.getVar('BUILD_MANIFEST_FIELDS_DEPLOY').split():
         key='DIST_BB_{0}'.format(field)
         allkeys.append(key)
         value=d.getVar(field)
         if value:
            f.write('{0}="{1}"\n'.format(key,value))
      f.write('declare -A BITBAKE_VARS\nBITBAKE_VARS[deploy]="%s"' % ' '.join(allkeys))

   with open(d.expand('${B}/bbinfo-target'),'w') as f:
      allkeys=[]
      for field in d.getVar('BUILD_MANIFEST_FIELDS_TARGET').split():
         key='DIST_BB_{0}'.format(field)
         allkeys.append(key)
         value=d.getVar(field)
         if value:
            f.write('{0}="{1}"\n'.format(key,value))
      f.write('declare -A BITBAKE_VARS\nBITBAKE_VARS[target]="%s"' % ' '.join(allkeys))

   with open(d.expand('${B}/bbinfo-sdk'),'w') as f:
      allkeys=[]
      for field in d.getVar('BUILD_MANIFEST_FIELDS_SDK').split():
         key='DIST_BB_{0}'.format(field)
         allkeys.append(key)
         value=d.getVar(field)
         if value:
            f.write('{0}="{1}"\n'.format(key,value))
      f.write('declare -A BITBAKE_VARS\nBITBAKE_VARS[sdk]="%s"' % ' '.join(allkeys))
}

do_compilestep2 () {
    rc=99
    timestamp=${DATETIME}
    outfile=${B}/build-info
    if [ -x "${DISTRO_MANIFEST_GENERATOR}" -a -f "${DISTRO_SETUP_MANIFEST}" ]; then
		rc=0
		for	format in bash json; do
			if [ "$format" = "json" ]; then
				ext=".json"
			else
				ext=""
			fi
			for mode in deploy target sdk; do
				${DISTRO_MANIFEST_GENERATOR} -m $mode -f $format -t $timestamp -s ${B}/bbinfo-${mode} ${DISTRO_SETUP_MANIFEST} >${outfile}-${mode}${ext}
				rc=$?
				if [ $rc -ne 0 ]; then
					break
				fi
			done
		done
    else
        if [ -z "${DISTRO_MANIFEST_GENERATOR}" ]; then
            echo "The name of the generation script is not defined."
        elif [ ! -f "${DISTRO_MANIFEST_GENERATOR}" ]; then
            echo "Generation script ${DISTRO_MANIFEST_GENERATOR} is missing."
        elif [ ! -x "${DISTRO_MANIFEST_GENERATOR}" ]; then
            echo "Generation script ${DISTRO_MANIFEST_GENERATOR} isn't executable."
        fi
        if [ -z "${DISTRO_SETUP_MANIFEST}" ]; then
            echo "The name of the data file is not defined."
        elif [ ! -f "${DISTRO_SETUP_MANIFEST}" ]; then
            echo "Data file ${DISTRO_SETUP_MANIFEST} is missing."
        fi
	echo "You can try to rerun aglsetup.sh to solve that issue."
	echo "You can also try to source agl-init-build-env instead of oe-init-build-env."
    fi

    if [ "$rc" -ne  0 ]; then
        echo "distro-build-manifest generation failed."
    fi
    return $rc
}

do_compilestep1[vardeps] += " ${BUILD_MANIFEST_FIELDS_DEPLOY}"
do_compilestep1[vardeps] += " ${BUILD_MANIFEST_FIELDS_TARGET}"
do_compilestep1[vardeps] += " ${BUILD_MANIFEST_FIELDS_SDK}"

# avoid errors "ERROR: When reparsing .../distro-build-manifest/distro-build-manifest.bb.do_compile, the basehash value changed from .... to .... . The metadata is not deterministic and this needs to be fixed."
do_compilestep2[vardepsexclude] = "DATETIME"

# combine the two steps
python do_compile() {
   bb.build.exec_func("do_compilestep1",d)
   bb.build.exec_func("do_compilestep2",d)
}

do_install () {
    # install in target dir
    install -d ${D}${sysconfdir}/platform-info
    install -m 0644 build-info-target ${D}${sysconfdir}/platform-info/build
    install -m 0644 build-info-target.json ${D}${sysconfdir}/platform-info/build.json

    # also copy in deploy dir
    install -d ${DEPLOY_DIR_IMAGE}
    install -m 0644 build-info-deploy ${DEPLOY_DIR_IMAGE}/build-info
    install -m 0644 build-info-deploy.json ${DEPLOY_DIR_IMAGE}/build-info.json

    # copy into sdk deploy dir
    install -d ${DEPLOY_DIR}/sdk
    install -m 0644 build-info-sdk ${DEPLOY_DIR}/sdk/${SDK_NAME}.build-info
    install -m 0644 build-info-sdk.json ${DEPLOY_DIR}/sdk/${SDK_NAME}.build-info.json

    # and copy to nativesdk package
    # TODO
}

# list of variables to add to the various manifests
# smalles one is 'target', then 'deploy' and finally 'sdk'
BUILD_MANIFEST_FIELDS_TARGET = "\
    MACHINE_ARCH \
    MACHINEOVERRIDES \
    MACHINE_FEATURES \
    DISTRO_CODENAME \
    DISTRO_FEATURES \
    DISTRO_BRANCH_VERSION_TAG \
    AGLVERSION \
    AGL_BRANCH \
    AGLRELEASETYPE \
"

BUILD_MANIFEST_FIELDS_DEPLOY = "\
    ${BUILD_MANIFEST_FIELDS_TARGET} \
    DISTRO \
    DISTRO_VERSION \
    DISTROOVERRIDES \
    TUNE_FEATURES \
    TUNE_PKGARCH \
    ALL_MULTILIB_PACKAGE_ARCHS \
"

BUILD_MANIFEST_FIELDS_SDK = "\
    ${BUILD_MANIFEST_FIELDS_DEPLOY} \
    HOST_SYS \
    TARGET_SYS \
    TARGET_VENDOR \
    SDK_ARCH \
    SDK_VENDOR \
    SDK_VERSION \
    SDK_OS \
"

# dont exec useless tasks
do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"


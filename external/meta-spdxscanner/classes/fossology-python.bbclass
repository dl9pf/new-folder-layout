# This class integrates real-time license scanning, generation of SPDX standard
# output and verifiying license info during the building process.
# It is a combination of efforts from the OE-Core, SPDX and fossology projects.
#
# For more information on fossology REST API:
#   https://www.fossology.org/get-started/basic-rest-api-calls/
#
# For more information on SPDX:
#   http://www.spdx.org
#
# Note:
# 1) Make sure fossology (after 3.5.0)(https://hub.docker.com/r/fossology/fossology/) has beed started on your host
# 2) spdx files will be output to the path which is defined as[SPDX_DEPLOY_DIR].
#    By default, SPDX_DEPLOY_DIR is tmp/deploy/
# 3) Added TOKEN has been set in conf/local.conf
#
inherit spdx-common 

#SPDXEPENDENCY += "python3-fossology-native:do_populate_sysroot"
#SPDXEPENDENCY += "python3-requests-native:do_populate_sysroot"

#CREATOR_TOOL = "fossology-python.bbclass in meta-spdxscanner"
FOSSOLOGY_SERVER ?= "http://127.0.0.1/repo"

# If ${S} isn't actually the top-level source directory, set SPDX_S to point at
# the real top-level directory.
SPDX_S ?= "${S}"

python do_spdx () {
    import os, sys, json, shutil

    pn = d.getVar('PN')
    assume_provided = (d.getVar("ASSUME_PROVIDED") or "").split()
    if pn in assume_provided:
        for p in d.getVar("PROVIDES").split():
            if p != pn:
                pn = p
                break

    # glibc-locale: do_fetch, do_unpack and do_patch tasks have been deleted,
    # so avoid archiving source here.
    if pn.startswith('glibc-locale'):
        return
    if (d.getVar('PN') == "libtool-cross"):
        return
    if (d.getVar('PN') == "libgcc-initial"):
        return
    if (d.getVar('PN') == "shadow-sysroot"):
        return

    # We just archive gcc-source for all the gcc related recipes
    if d.getVar('BPN') in ['gcc', 'libgcc']:
        bb.debug(1, 'spdx: There is bug in scan of %s is, do nothing' % pn)
        return

    spdx_outdir = d.getVar('SPDX_OUTDIR')
    spdx_workdir = d.getVar('SPDX_WORKDIR')
    spdx_temp_dir = os.path.join(spdx_workdir, "temp")
    temp_dir = os.path.join(d.getVar('WORKDIR'), "temp")
    
    info = {} 
    info['workdir'] = (d.getVar('WORKDIR') or "")
    info['pn'] = (d.getVar( 'PN') or "")
    info['pv'] = (d.getVar( 'PV') or "")
    info['package_download_location'] = (d.getVar( 'SRC_URI') or "")
    if info['package_download_location'] != "":
        info['package_download_location'] = info['package_download_location'].split()[0]
    info['spdx_version'] = (d.getVar('SPDX_VERSION') or '')
    info['data_license'] = (d.getVar('DATA_LICENSE') or '')
    info['creator'] = {}
    info['creator']['Tool'] = (d.getVar('CREATOR_TOOL') or '')
    info['license_list_version'] = (d.getVar('LICENSELISTVERSION') or '')
    info['package_homepage'] = (d.getVar('HOMEPAGE') or "")
    info['package_summary'] = (d.getVar('SUMMARY') or "")
    info['package_summary'] = info['package_summary'].replace("\n","")
    info['package_summary'] = info['package_summary'].replace("'"," ")
    info['package_contains'] = (d.getVar('CONTAINED') or "")
    info['package_static_link'] = (d.getVar('STATIC_LINK') or "")
    info['modified'] = "false"
    srcuri = d.getVar("SRC_URI", False).split()
    length = len("file://")
    for item in srcuri:
        if item.startswith("file://"):
            item = item[length:]
            if item.endswith(".patch") or item.endswith(".diff"):
                info['modified'] = "true"

    manifest_dir = (d.getVar('SPDX_DEPLOY_DIR') or "")
    if not os.path.exists( manifest_dir ):
        bb.utils.mkdirhier( manifest_dir )

    info['outfile'] = os.path.join(manifest_dir, info['pn'] + "-" + info['pv'] + ".spdx" )
    sstatefile = os.path.join(spdx_outdir, info['pn'] + "-" + info['pv'] + ".spdx" )
    
    # if spdx has been exist
    if os.path.exists(info['outfile']):
        bb.note(info['pn'] + "spdx file has been exist, do nothing")
        return
    if os.path.exists( sstatefile ):
        bb.note(info['pn'] + "spdx file has been exist, do nothing")
        create_manifest(info,sstatefile)
        return

    spdx_get_src(d)

    bb.note('SPDX: Archiving the patched source...')
    if os.path.isdir(spdx_temp_dir):
        for f_dir, f in list_files(spdx_temp_dir):
            temp_file = os.path.join(spdx_temp_dir,f_dir,f)
            shutil.copy(temp_file, temp_dir)
    
    d.setVar('WORKDIR', spdx_workdir)
    info['sourcedir'] = spdx_workdir
    git_path = "%s/git/.git" % info['sourcedir']
    if os.path.exists(git_path):
        remove_dir_tree(git_path)
    tar_name = spdx_create_tarball(d, d.getVar('WORKDIR'), 'patched', spdx_outdir)
    ## get everything from cache.  use it to decide if 
    ## something needs to be rerun
    if not os.path.exists(spdx_outdir):
        bb.utils.mkdirhier(spdx_outdir)
    cur_ver_code = get_ver_code(spdx_workdir).split()[0] 
    ## Get spdx file
    bb.note(" Begin to get spdxx file ...... ")
    if not os.path.isfile(tar_name):
        bb.note(info['pn'] + "has no source, do nothing")
        return

    invoke_fossology_python(d, tar_name, sstatefile)
    if get_cached_spdx(sstatefile) != None:
        write_cached_spdx( info,sstatefile,cur_ver_code )
        ## CREATE MANIFEST(write to outfile )
        create_manifest(info,sstatefile)
    else:
        bb.error('Can\'t get the spdx file ' + info['pn'] + '. Please check your.')
    remove_file(tar_name)
}
def upload_oss(d, folder, foss, filepath):
    import os
    import subprocess
    from fossology.exceptions import AuthorizationError, FossologyApiError
    from tenacity import TryAgain
    
    wait_time = 60

    from fossology.obj import AccessLevel

    (work_dir, filename) = os.path.split(filepath)

    upload_list = foss.list_uploads()
    upload = None

    bb.note("Check folder, %s ,has been uploaded?" % filename)
    bb.note("%s" % folder.name)
    for upload in upload_list:
        bb.note("uploaded name = %s" % upload.uploadname)
        if upload.uploadname == filename and upload.foldername == folder.name:
            bb.note("The size of uploaded fileis %s" % upload.filesize)
            bb.note("%s has uploaded, won't upload agin" % filename)
            return upload
    try:
        upload = foss.upload_file(
        folder,
        file=filepath,
        access_level=AccessLevel.PUBLIC,
        )
    except AttributeError as error:
        bb.error(error.message)
    except FossologyApiError as error:
        bb.error(error.message)
    except TryAgain:
        bb.warn("Upload is still not ready, try again.")
        while i < 3:
            time.sleep(wait_time)
            try:
                foss.detail_upload(upload.id)
            except TryAgain:
                continue
            bb.error("upload_oss result: upload.filesize = %s." % upload.filesize)
            bb.note("The result of upload is : %s " % upload)
            return upload
        bb.error("Upload still not complete.")
    
def create_folder(d, foss, token, folder_name):
    bb.note("create_folder :")
    bb.note("parent = %s" % foss.rootFolder)
    bb.note("new folder = %s" % folder_name)
    folder = foss.create_folder(foss.rootFolder, folder_name, description=None)
    if folder.name != folder_name:
        bb.error("Folder %s couldn't be created" % folder_name)
    #bb.warn("folder id = %s" % folder.id)
    return folder
  
def start_schedule_jobs(d, folder, foss, upload_oss, upload_filename, wait=False, timeout=30):
    from fossology.exceptions import FossologyApiError
    from fossology.obj import Agents
    job = None
    job_wait = 120
    i = 0

    try:
        analysis_agents = foss.user.agents.to_dict()
    except AttributeError:
        # Create default user agents
        foss.user.agents = Agents(True, True, False, False, True, True, True, True, True,)
        analysis_agents = foss.user.agents.to_dict()
    jobs_spec = {
        "analysis": analysis_agents,
        "decider": {
            "nomos_monk": True,
            "bulk_reused": True,
            "new_scanner": True,
            "ojo_decider": True,
        },
        "reuse": {
            "reuse_upload": 0,
            "reuse_group": 0,
            "reuse_main": True,
            "reuse_enhanced": True,
        },
    }
    jobs = foss.list_jobs(upload=upload_oss)
    if jobs is None:
        try:
            job = foss.schedule_jobs(folder, upload_oss, jobs_spec)
            if job.name != upload_filename:
                bb.error("Job %s does not relate to the correct upload" % job.name )
        except FossologyApiError as error:
            bb.error(error.message)
    elif len(jobs) < 2:
        try:
            job = foss.schedule_jobs(folder, upload_oss, jobs_spec)
            if job.name != upload_filename:
                bb.error("Job %s does not relate to the correct upload" % job.name )
        except FossologyApiError as error:
            bb.error(error.message)
    else:
        bb.note("%s jobs has existed for updated %s " % (len(jobs), upload_filename))

    jobs = foss.list_jobs(upload=upload_oss)
    bb.note("jobs[0].uploadId = %s" % jobs[0].uploadId)
    while i < 5:
        i += 1
        if len(jobs) < 2:
            bb.warn("%s jobs start not completed " % upload_filename)
            time.sleep(30)
        else:
            break

    jobs = foss.list_jobs(upload=upload_oss)
    if len(jobs) < 2:
        bb.error("%s jobs start not completed,please checkout fossology server." % upload_filename)

    job = foss.detail_job(jobs[1].id, wait=True, timeout=job_wait)
    if job.status != "Completed":
        bb.error("The Job is still not completed yet. Please comfirm your fossology server.")
    else:
        bb.note("%s jobs has completed." % upload_filename)
        
    return job

def get_report(d, foss, upload, report_name):
    import os 
    import time
    from fossology.exceptions import AuthorizationError, FossologyApiError
    from tenacity import TryAgain
    from fossology.obj import ReportFormat
    
    i = 0
    wait_time = 60
    complete = False
    report_id = None
    report = None

    try:
        report_id = foss.generate_report(
            upload, report_format=ReportFormat.SPDX2TV
        )
    except FossologyApiError as error:
        bb.error(error.message)
    except AuthorizationError as error:
        bb.error(error.message)
    except AttributeError as error:
        bb.error("AttributeError! It seems fossology server is busy.")

    if report_id == None:
        bb.error("Get report id failed! Maybe fossology server is busy.")
        return
    if os.path.isfile(report_name):
        os.remove(report_name)
    
    while i < 4:
        i += 1
        try:
            # Plain text
            report = foss.download_report(report_id)
        except TryAgain:
            bb.warn("SPDX file is still not ready, try again.")
            time.sleep(wait_time)
            continue
        except FossologyApiError as error:
            bb.warn("Dowload SPDX file failed, Try again.")
            continue
        if report != None:
            break
    if report == None:   
        bb.error("Get report id failed! Maybe fossology server is busy.")
    else:
        with open(report_name, "w+") as report_file:
            report_file.write(report)

def invoke_fossology_python(d, tar_file, spdx_file):
    import os
    import re
    import time
    import logging

    from fossology import Fossology, fossology_token
    from fossology.obj import TokenScope
    from fossology.exceptions import FossologyApiError, AuthenticationError
    from tenacity import retry, TryAgain, stop_after_attempt

    logger = logging.getLogger()
    logger.setLevel(logging.INFO)
    logging.basicConfig(level=logging.INFO)

    wait_time = 200
    i = 0
    complete = False

    fossology_server = d.getVar('FOSSOLOGY_SERVER')
    token = d.getVar('TOKEN')

    (work_dir, tar_file) = os.path.split(tar_file)
    os.chdir(work_dir)

    #if 'http_proxy' in os.environ:
    #    del os.environ['http_proxy']
    bb.note("invoke_fossdriver : tar_file = %s " % tar_file)
    foss = Fossology(fossology_server, token, "fossy")

    if d.getVar('FOLDER_NAME', False):
        folder_name = d.getVar('FOLDER_NAME')
        folder = create_folder(d, foss, token, folder_name)
    else:
        folder = foss.rootFolder
   
    bb.note("Begin to upload.")
    upload = upload_oss(d, folder, foss, tar_file)
    job = start_schedule_jobs(d, folder, foss, upload, tar_file)
    time.sleep(wait_time)
    while i < 3:
        i += 1
        try:
            job_dtl = foss.detail_job(job.id, wait=True, timeout=60)
        except FossologyApiError as error:
            bb.note("Job has not completed.")
            time.sleep(wait_time)
            continue
        bb.note("Job for %s is completed." % tar_file)
        break
    time.sleep(wait_time)
    i = 0
    while i < 2:
        i += 1
        get_report(d, foss, upload, spdx_file)
        if bb.data.inherits_class('packagegroup',d):
            return True
        if bb.data.inherits_class('image',d):
            return True

        file = open(spdx_file,'r+')
        line = file.readline()
        while line:
            if "LicenseID:" in line:
                complete = True
                break
            line = file.readline()
        file.close()
        if complete == False:
            bb.warn("license info not complete, try agin.")
            time.sleep(wait_time)            
        else:
            return True

EXPORT_FUNCTIONS do_spdx

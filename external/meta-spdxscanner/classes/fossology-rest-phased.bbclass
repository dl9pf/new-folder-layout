# This class integrates real-time license scanning, generation of SPDX standard
# output and verifiying license info during the building process.
# It is a combination of efforts from the OE-Core, SPDX and DoSOCSv2 projects.
#
# For more information on DoSOCSv2:
#   https://github.com/DoSOCSv2
#
# For more information on SPDX:
#   http://www.spdx.org
#
# Note:
# 1) Make sure fossdriver has beed installed in your host
# 2) By default,spdx files will be output to the path which is defined as[SPDX_DEPLOY_DIR] 
#    in ./meta/conf/spdx-dosocs.conf.
inherit spdx-common
FOSSOLOGY_SERVER ?= "http://127.0.0.1:8081/repo"
FOSSOLOGY_REUPLOAD ??= "0"

#upload OSS into No.1 folder of fossology
FOSSOLOGY_FOLDER_ID ??= "1"
FOSSOLOGY_FOLDER_NAME ??= "${DISTRO_CODENAME}-${DISTRO_VERSION}"

FOSSOLOGY_EXCLUDE_PACKAGES ??= "glibc-locale libtool-cross libgcc-initial shadow-sysroot"
FOSSOLOGY_EXCLUDE_NATIVE ??= "1"
FOSSOLOGY_EXCLUDE_SDK ??= "1"
# translate to common variables
SPDX_EXCLUDE_PACKAGES := "${FOSSOLOGY_EXCLUDE_PACKAGES}"
SPDX_EXCLUDE_NATIVE := "${FOSSOLOGY_EXCLUDE_NATIVE}"
SPDX_EXCLUDE_SDK := "${FOSSOLOGY_EXCLUDE_SDK}"


HOSTTOOLS_NONFATAL += "curl quilt unzip"

CREATOR_TOOL = "fossology-rest-phased.bbclass in meta-spdxscanner"

# If ${S} isn't actually the top-level source directory, set SPDX_S to point at
# the real top-level directory.
SPDX_S ?= "${S}"

def populate_info(d, info):
    info['workdir'] = (d.getVar('WORKDIR', True) or "")
    info['pn'] = (d.getVar( 'PN', True ) or "")
    info['pv'] = (d.getVar( 'PV', True ) or "")
    info['package_download_location'] = (d.getVar( 'SRC_URI', True ) or "")
    if info['package_download_location'] != "":
        info['package_download_location'] = info['package_download_location'].split()[0]
    info['spdx_version'] = (d.getVar('SPDX_VERSION', True) or '')
    info['data_license'] = (d.getVar('DATA_LICENSE', True) or '')
    info['creator'] = {}
    info['creator']['Tool'] = (d.getVar('CREATOR_TOOL', True) or '')
    info['license_list_version'] = (d.getVar('LICENSELISTVERSION', True) or '')
    info['package_homepage'] = (d.getVar('HOMEPAGE', True) or "")
    info['package_summary'] = (d.getVar('SUMMARY', True) or "")
    info['package_summary'] = info['package_summary'].replace("\n","")
    info['package_summary'] = info['package_summary'].replace("'"," ")
    info['package_contains'] = (d.getVar('CONTAINED', True) or "")
    info['package_static_link'] = (d.getVar('STATIC_LINK', True) or "")
    info['modified'] = "false"
    info['token'] = (d.getVar('FOSSOLOGY_TOKEN', True) or "")
    info['manifest_dir'] = (d.getVar('SPDX_DEPLOY_DIR', True) or "")
    info['srcuri'] = d.getVar("SRC_URI", False).split()
    length = len("file://")
    for item in info['srcuri']:
        if item.startswith("file://"):
            item = item[length:]
            if item.endswith(".patch") or item.endswith(".diff"):
                info['modified'] = "true"
    info['spdx_outdir'] = d.getVar('SPDX_OUTDIR')
    info['spdx_workdir'] = d.getVar('SPDX_WORKDIR')
    info['spdx_temp_dir'] = os.path.join(info['spdx_workdir'], "temp")
    info['temp_dir'] = os.path.join(d.getVar('WORKDIR'), "temp")
    info['outfile'] = os.path.join(info['manifest_dir'], info['pn'] + "-" + info['pv'] + ".spdx" )
    info['sstatefile'] = os.path.join(info['spdx_outdir'], info['pn'] + "-" + info['pv'] + ".spdx" )

def prepare_folders(info):
    if not os.path.exists( info['manifest_dir'] ):
        bb.utils.mkdirhier( info['manifest_dir'] )
    if not os.path.exists(info['spdx_outdir']):
        bb.utils.mkdirhier(info['spdx_outdir'])


def use_cached_files(info):
    # if spdx sstate or report already exist
    if os.path.exists(info['outfile']):
        bb.warn(info['pn'] + "The spdx report file already exists, do nothing.")
        return
    if os.path.exists( info['sstatefile'] ):
        bb.warn(info['pn'] + "The spdx sstate file already exists, do nothing.")
        create_manifest(info, info['sstatefile'])
        return

def fossologyupload(d, bb, info):
    import os, sys, shutil

    pn = d.getVar('PN', True)
    # exclude packages not necessary (-native, nativesdk-)  or on blacklist
    if excluded_package(d, pn):
        bb.note("spdx: fossologyanalyze: excluding "+ pn)
        return

    # retrieve the folder id
    info['folder_id'] = get_folder_id(d)

    # prepare folders
    prepare_folders(info)

    # get source
    spdx_get_src(d)

    bb.note('spdx: Archiving the patched source...')
    if os.path.isdir(info['spdx_temp_dir']):
        for f_dir, f in list_files(info['spdx_temp_dir']):
            temp_file = os.path.join(info['spdx_temp_dir'],f_dir,f)
            shutil.copy(temp_file, info['temp_dir'])
        shutil.rmtree(info['spdx_temp_dir'])
    d.setVar('WORKDIR', info['spdx_workdir'])
    info['sourcedir'] = info['spdx_workdir']
    git_path = "%s/git/.git" % info['sourcedir']
    if os.path.exists(git_path):
        remove_dir_tree(git_path)

    tar_name = spdx_create_tarball(d, d.getVar('WORKDIR'), 'patched', info['spdx_outdir'])

    ## get everything from cache.  use it to decide if
    ## something needs to be rerun
    cur_ver_code = get_ver_code(info['spdx_workdir']).split()[0] 
    # upload archive
    if invoke_rest_api_upload(d, tar_name, info['outfile'], info['folder_id']) == False:
        bb.error(info['pn'] + ": Upload failed, please check fossology server.")
    return


def fossologyanalyse(d, bb, info):
    import os, sys, shutil

    pn = d.getVar('PN', True)
    # exclude packages not necessary (-native, nativesdk-)  or on blacklist
    if excluded_package(d, pn):
        bb.note("spdx: fossologyanalyze: excluding "+ pn)
        return


    # retrieve the folder id
    info['folder_id'] = get_folder_id(d)

    # prepare folders
    prepare_folders(info)

    # TODO: put into common func or info[]
    filename = '%s-%s.tar.gz' % (d.getVar('PF'), 'patched')
    tar_name = os.path.join(info['spdx_outdir'], filename)

    # invoce the analysis
    if invoke_rest_api_analysis(d, tar_name, info['sstatefile'], info['folder_id']) == False:
        bb.error("spdx: " + info['pn'] + ": Analysis trigger failed, please check fossology server.")
    return


def fossologytrigger(d, bb, info):
    import os, sys, shutil

    pn = d.getVar('PN', True)
    # exclude packages not necessary (-native, nativesdk-)  or on blacklist
    if excluded_package(d, pn):
        bb.note("spdx: fossologyanalyze: excluding "+ pn)
        return

    # setup variables
    info = {}
    populate_info(d, info)

    # retrieve the folder id
    info['folder_id'] = get_folder_id(d)

    # TODO: put into common func or info[]
    filename = '%s-%s.tar.gz' % (d.getVar('PF'), 'patched')
    tar_name = os.path.join(info['spdx_outdir'], filename)

    report_id = invoke_rest_api_triggerreport(d, tar_name, info['outfile'], info['folder_id'])
    if report_id == False:
        bb.error("spdx:" + info['pn'] + ": trigger_spdx failed.")
        return False
    else:
        info['report_id'] = report_id
        d.setVar('spdx_report_id', info['report_id'])
        return True


def fossologyreport(d, bb, info):
    import os, sys, shutil

    pn = d.getVar('PN', True)
    # exclude packages not necessary (-native, nativesdk-)  or on blacklist
    if excluded_package(d, pn):
        bb.note("spdx: fossologyanalyze: excluding "+ pn)
        return

    if not 'folder_id' in info:
        # retrieve the folder id
        info['folder_id'] = get_folder_id(d)
    if not 'report_id' in info:
        x = d.getVar("spdx_report_id")
        if not x:
            bb.error("spdx: no report_id")
            return False
        info['report_id'] = x

    # TODO: put into common func or info[]
    filename = '%s-%s.tar.gz' % (d.getVar('PF'), 'patched')
    tar_name = os.path.join(info['spdx_outdir'], filename)

    bb.warn("outfile: " + str(info['outfile']))

    if invoke_rest_api_getresult(d, tar_name, info['outfile'], info['folder_id'], info['report_id']) == False:
        bb.error("spdx: " + info['pn'] + ": get_spdx failed.")
    return


def fossologywaitextracted(d, bb, info):
    import time
    import subprocess
    import os
    delaytime = 120
    i = 0

    pn = d.getVar('PN', True)
    # exclude packages not necessary (-native, nativesdk-)  or on blacklist
    if excluded_package(d, pn):
        bb.note("spdx: fossologyanalyze: excluding "+ pn)
        return

    # setup variables
    info = {}
    populate_info(d, info)

    # retrieve the folder id
    info['folder_id'] = get_folder_id(d)

    # TODO: put into common func or info[]
    filename = '%s-%s.tar.gz' % (d.getVar('PF'), 'patched')
    tar_name = os.path.join(info['spdx_outdir'], filename)

    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('FOSSOLOGY_TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting FOSSOLOGY_TOKEN!\n")
        raise OSError(errno.ENOENT, "No setting of FOSSOLOGY_TOKEN comes from fossology server.")

    upload_id = has_upload(d, tar_name, info['folder_id'], True)
    bb.warn("spdx: upload_id :" + str(upload_id))
    if upload_id:
        info['upload_id'] = upload_id

        while i < 360:
            #api/v1/jobs?upload=id
            rest_api_cmd = "curl -k -s -S -X GET " + server_url + "/api/v1/jobs?upload=" \
                           + str(upload_id) \
                           + " -H \"Authorization: Bearer " + token + "\"" \
                           + " --noproxy 127.0.0.1"
            bb.note("Invoke rest_api_cmd = " + rest_api_cmd)
            try:
                jobstatus = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
            except subprocess.CalledProcessError as e:
                bb.error(d.getVar('PN', True) + ": Could not get job status: \n%s" % e.output.decode("utf-8"))
                return
            jobstatus = str(jobstatus, encoding = "utf-8")
            jobstatus = eval(jobstatus)
            bb.note(str(jobstatus))
            if len(jobstatus) == 0:
                bb.warn("The jobstatus is 0." + str(jobstatus))
                return False
            ret=0
            for i in range(0, len(jobstatus)):
                bb.warn(str(jobstatus[i]))
                # wait for any job to be complete before proceeding
                if jobstatus[i]["status"] == "Completed":
                    bb.note("Job part complete.")
                else:
                    ret += 1
            if ret == 0:
                bb.warn("Job complete.")
                return
            i += 1
            time.sleep(delaytime)
    else:
        bb.error("No upload_id")
        return

addtask spdxupload
do_spdxupload[depends] = "${SPDXEPENDENCY}"
do_spdxupload[deptask] += "do_fetch do_unpack do_patch"
python do_spdxupload() {
    # setup variables
    info = {}
    populate_info(d, info)

    fossologyupload(d, bb, info)
}


addtask spdxwaitextracted
python do_spdxwaitextracted() {
    # setup variables
    info = {}
    populate_info(d, info)
    fossologywaitextracted(d, bb, info)
}

addtask spdxanalyse
python do_spdxanalyse() {
    # setup variables
    info = {}
    populate_info(d, info)
    #fossologywaitextracted(d, bb, info)
    fossologyanalyse(d, bb, info)
}

addtask spdxreport
python do_spdxreport(){
    import time
    # setup variables
    info = {}
    populate_info(d, info)

    if fossologytrigger(d, bb, info):
        fossologyreport(d, bb, info)
}

python do_spdx() {

    pn = d.getVar('PN')
    info = {}
    populate_info(d, info)

    fossologyupload(d, bb, info)

    fossologywaitextracted(d, bb, info, True)

    fossologyanalyse(d, bb, info)

    if fossologytrigger(d, bb, info):
        fossologyreport(d, bb, info)

}


def get_folder_id_by_name(d, folder_name):
    import os
    import subprocess
    import json

    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('FOSSOLOGY_TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting FOSSOLOGY_TOKEN!\n")
        raise OSError(errno.ENOENT, "No setting of FOSSOLOGY_TOKEN comes from fossology server.")

    rest_api_cmd = "curl -k -s -S -X GET " + server_url + "/api/v1/folders" \
                   + " -H \"Authorization: Bearer " + token + "\"" \
                   + " --noproxy 127.0.0.1"
    bb.note("Invoke rest_api_cmd = " + rest_api_cmd )
    try:
        all_folder = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
    except subprocess.CalledProcessError as e:
        bb.error(d.getVar('PN', True) + ": Get folder list failed: \n%s" % e.output.decode("utf-8"))
        return False
    all_folder = str(all_folder, encoding = "utf-8")
    all_folder = json.loads(all_folder)
    if len(all_folder) == 0:
        bb.note("Can not get folder list.")
        return False
    bb.note("all_folder[0][name] = " + all_folder[0]["name"])
    for i in range(0, len(all_folder)):
        if all_folder[i]["name"] == folder_name:
                bb.note("Found " + folder_name + "on fossology server.")
                return all_folder[i]["id"]
    return False

def create_folder(d, folder_name):
    import os
    import subprocess

    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('FOSSOLOGY_TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting FOSSOLOGY_TOKEN!\n")
        raise OSError(errno.ENOENT, "No setting of FOSSOLOGY_TOKEN comes from fossology server.")

    rest_api_cmd = "curl -k -s -S -X POST " + server_url + "/api/v1/folders" \
                   + " -H \'parentFolder: 1\'" \
                   + " -H \'folderName: " + folder_name + "\'" \
                   + " -H \"Authorization: Bearer " + token + "\"" \
                   + " --noproxy 127.0.0.1"
    bb.note("Invoke rest_api_cmd = " + rest_api_cmd)
    try:
        add_folder = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
    except subprocess.CalledProcessError as e:
        bb.error(d.getVar('PN', True) + ": Added folder failed: \n%s" % e.output.decode("utf-8"))
        return False

    add_folder = str(add_folder, encoding = "utf-8")
    add_folder = eval(add_folder)
    if str(add_folder["code"]) == "201":
        bb.note("add_folder = " + folder_name)
        return add_folder["message"]
    elif str(add_folder["code"]) == "200":
        bb.note("Folder : " + folder_name + "has been created.")
        return get_folder_id_by_name(d, folder_name)
    else:
        bb.error(d.getVar('PN', True) + ": Added folder failed, please check your fossology server.")
        return False

def get_folder_id(d):
    if d.getVar('FOSSOLOGY_FOLDER_NAME', False):
        folder_name = d.getVar('FOSSOLOGY_FOLDER_NAME')
        folder_id = create_folder(d, folder_name)
    else:
        folder_id = (d.getVar('FOSSOLOGY_FOLDER_ID', True) or "1")
    bb.note("Folder Id =  " + str(folder_id))
    return str(folder_id)

def has_upload(d, tar_file, folder_id, skipupload=False):
    import os
    import subprocess

    (work_dir, file_name) = os.path.split(tar_file) 

    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('FOSSOLOGY_TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting FOSSOLOGY_TOKEN!\n")
        raise OSError(errno.ENOENT, "No setting of TOKEN comes from fossology server.")

    rest_api_cmd = "curl -k -s -S -X GET " + server_url + "/api/v1/uploads" \
                   + " -H \"Authorization: Bearer " + token + "\"" \
                   + " --noproxy 127.0.0.1"
    bb.note("Invoke rest_api_cmd = " + rest_api_cmd )
    try:
        upload_output = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
    except subprocess.CalledProcessError as e:
        bb.error("curl failed: \n%s" % e.output.decode("utf-8"))
        return False

    upload_output = str(upload_output, encoding = "utf-8")
    upload_output = eval(upload_output)
    if len(upload_output) == 0:
        bb.warn("The upload of fossology is 0.")
        return False
    for i in range(0, len(upload_output)):
        if upload_output[i]["uploadname"] == file_name and str(upload_output[i]["folderid"]) == str(folder_id):
            if d.getVar('FOSSOLOGY_REUPLOAD') == "1" and not skipupload:
                bb.warn("#### Reupload triggered ####")
                return False
            #bb.warn("########### upload_output[i][uploadname] = " + upload_output[i]["uploadname"])
            #bb.warn("########### Found " + file_name + " on fossology server \"Software Repository\" folder. Will skip upload.")
            return upload_output[i]["id"]
    return False

def upload(d, tar_file, folder):
    import os
    import subprocess
    import time
    delaytime = 50
    i = 0
    j = 0
 
    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('FOSSOLOGY_TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting FOSSOLOGY_TOKEN!\n")
        raise OSError(errno.ENOENT, "No setting of TOKEN comes from fossology server.")
    
    rest_api_cmd = "curl -k -s -S -X POST " + server_url + "/api/v1/uploads" \ 
                    + " -H \"folderId: " + folder + "\"" \
                    + " -H \"Authorization: Bearer " + token + "\"" \
                    + " -H \'uploadDescription: created by REST\'" \
                    + " -H \'public: public\'"  \
                    + " -H \'Content-Type: multipart/form-data\'"  \
                    + " -F \'fileInput=@\"" + tar_file + "\";type=application/octet-stream\'" \
                    + " --noproxy 127.0.0.1"
    bb.note("Upload : Invoke rest_api_cmd = " + rest_api_cmd )
    while i < 10:
        try:
            upload = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
        except subprocess.CalledProcessError as e:
            bb.error(d.getVar('PN', True) + ": Upload failed: \n%s" % e.output.decode("utf-8"))
            return False
        upload = str(upload, encoding = "utf-8")
        if not upload:
            bb.error("No upload response")
            time.sleep(delaytime)
            continue
        if "ERROR" in str(upload):
            bb.warn("ERROR in upload")
            time.sleep(delaytime)
            continue
        if "Error" in str(upload):
            bb.warn("Error in upload")
            time.sleep(delaytime)
            continue
        if "error" in str(upload):
            bb.warn("error in upload")
            time.sleep(delaytime)
            continue
        if "504 Gateway Time-out" in str(upload):
            bb.warn("504 Gateway Timeout in upload")
            time.sleep(delaytime)
            continue
        upload = eval(upload)
        if str(upload["code"]) == "201":
            return upload["message"]
        i += 1
        time.sleep(delaytime)
    bb.error("spdx: " + d.getVar('PN', True) + ": Upload failed, please check your fossology server connection.")
    bb.error(str(upload))
    return False

def analysis(d, folder_id, upload_id):
    import os
    import subprocess
    delaytime = 50
    i = 0

    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('FOSSOLOGY_TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting FOSSOLOGY_TOKEN!\n")
        raise OSError(errno.ENOENT, "No setting of FOSSOLOGY_TOKEN comes from fossology server.")

    rest_api_cmd = "curl -k -s -S -X POST " + server_url + "/api/v1/jobs" \
                    + " -H \"folderId: " + str(folder_id) + "\"" \
                    + " -H \"uploadId: " + str(upload_id) + "\"" \
                    + " -H \"Authorization: Bearer " + token + "\"" \
                    + " -H \'Content-Type: application/json\'" \
                    + " --data \'{\"analysis\": {\"bucket\": true,\"copyright_email_author\": true,\"ecc\": true, \"keyword\": true,\"mime\": true,\"monk\": true,\"nomos\": true,\"ojo\": true,\"package\": true},\"decider\": {\"nomos_monk\": true,\"new_scanner\": true}}\'" \
                    + " --noproxy 127.0.0.1"
    bb.warn("Analysis : Invoke rest_api_cmd = " + rest_api_cmd )
    try:
        analysis = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
    except subprocess.CalledProcessError as e:
        bb.error("Analysis failed: \n%s" % e.output.decode("utf-8"))
        return False
    analysis = str(analysis, encoding = "utf-8")
    analysis = eval(analysis)
    if str(analysis["code"]) == "201":
        return analysis["message"]
    elif str(analysis["code"]) == "404":
        bb.error("spdx: " + d.getVar('PN', True) + ": cannot trigger analysis as extraction failed.")
        bb.error(str(analysis))
        return False
    else:
        bb.error("spdx: analysis trigger failed: " + str(analysis))
        return False

def trigger(d, folder_id, upload_id):
    import os
    import subprocess
    delaytime = 50
    i = 0

    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('FOSSOLOGY_TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting FOSSOLOGY_TOKEN!\n")
        raise OSError(errno.ENOENT, "No setting of FOSSOLOGY_TOKEN comes from fossology server.")

    rest_api_cmd = "curl -k -s -S -X GET " + server_url + "/api/v1/report" \
                    + " -H \"Authorization: Bearer " + token + "\"" \
                    + " -H \"uploadId: " + str(upload_id) + "\"" \
                    + " -H \'reportFormat: spdx2tv\'" \
                    + " --noproxy 127.0.0.1"
    bb.note("trigger : Invoke rest_api_cmd = " + rest_api_cmd )
    while i < 10:
        time.sleep(10)
        try:
            trigger = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
        except subprocess.CalledProcessError as e:
            bb.error(d.getVar('PN', True) + ": Trigger failed: \n%s" % e.output.decode("utf-8"))
            return False
        trigger = str(trigger, encoding = "utf-8")
        trigger = eval(trigger)
        if str(trigger["code"]) == "201":
            return trigger["message"].split("/")[-1]
        i += 1
        time.sleep(delaytime * 2)
        bb.note("spdx: " + d.getVar('PN', True) + ": Trigger failed, will try again.")
    bb.note("spdx: " + d.getVar('PN', True) + ": Trigger failed, please check your fossology server.")
    return False

def get_spdx(d, report_id, spdx_file):
    import os
    import subprocess
    import time
    delaytime = 10
    complete = False
    i = 0

    # Wait 20 seconds for the report to be generated on the server
    time.sleep(20)

    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('FOSSOLOGY_TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting FOSSOLOGY_TOKEN!\n")
        raise OSError(errno.ENOENT, "No setting of FOSSOLOGY_TOKEN comes from fossology server.")
    rest_api_cmd = "curl -k -s -S -X GET " + server_url + "/api/v1/report/" + report_id \
                    + " -H \'accept: text/plain\'" \
                    + " -H \"Authorization: Bearer " + token + "\"" \
                    + " --noproxy 127.0.0.1"
    bb.note("get_spdx : Invoke rest_api_cmd = " + rest_api_cmd )
    while i < 24:
        file = open(spdx_file,'wt')
        try:
            p = subprocess.Popen(rest_api_cmd, shell=True, universal_newlines=True, stdout=file).wait()
        except subprocess.CalledProcessError as e:
            bb.error("Get spdx failed: \n%s. Please check fossology server." % e.output.decode("utf-8"))
            file.close()
            os.remove(spdx_file)
            return False
        file.flush()
        file.close()
        time.sleep(2)
        file = open(spdx_file,'r+')
        first_line = file.readline()
        bb.note("SPDX_FILE first line: " + str(first_line))
        if "SPDXVersion" in first_line:
            complete = True
        file.close()

        if complete == True:
            return True
        else:
            bb.note("spdx: " + d.getVar('PN', True) + ": Get the first line is " + first_line + ". Will try again.")
        i += 1
        time.sleep(delaytime)
        delaytime = delaytime + 10
    file.close()
    bb.error("spdx: " + d.getVar('PN', True) + ": SPDX report could not be downloaded.")

def invoke_rest_api_upload(d, tar_file, spdx_file, folder_id):
    import os
    import time
    i = 0
    bb.note("invoke fossology REST API : tar_file = %s " % tar_file)
    upload_id = has_upload(d, tar_file, folder_id, False)
    if upload_id == False:
        bb.warn("This OSS has not been scanned. So upload it to fossology server.")
        upload_id = upload(d, tar_file, folder_id)
        if upload_id == False:
            return False
    return True

def invoke_rest_api_analysis(d, tar_file, spdx_file, folder_id):
    upload_id = has_upload(d, tar_file, folder_id, True)
    if upload_id == False:
        if d.getVar('FOSSOLOGY_REUPLOAD') == "1":
            bb.note("Reupload.")
            upload_id = upload(d, tar_file, folder_id)
            if upload_id == False:
                return False
        else:
            bb.note("This OSS has not been uploaded. Skip it.")
            return False
    if analysis(d, folder_id, upload_id) == False:
        bb.note("Analysis failed.")
        return False
    bb.note("Analysis success.")
    return True

def invoke_rest_api_triggerreport(d, tar_file, spdx_file, folder_id):
    import time
    i = 0

    upload_id = has_upload(d, tar_file, folder_id, True)
    if upload_id == False:
        bb.error("Could not find the file on the fossology server!")
        return False

    while i < 3:
        i += 1
        report_id = trigger(d, folder_id, upload_id)
        bb.note("Report_id: " + str(report_id))
        if report_id:
            return report_id
        time.sleep(30)

    bb.error("Could not trigger the report generation for " + d.getVar('PN', True))
    return False

def invoke_rest_api_getresult(d, tar_file, spdx_file, folder_id, report_id):
    i = 0

    upload_id = has_upload(d, tar_file, folder_id, True)
    if upload_id == False:
        bb.error("No upload of this software found on the fossology server!")
        return False

    while i < 3:
        i += 1
        spdx2tv = get_spdx(d, report_id, spdx_file)
        if spdx2tv == False:
            bb.note("spdx : " + d.getVar('PN', True) + ": get_spdx failed. Will try again!")
        else:
            return True

    bb.error("spdx: get_spdx of " + d.getVar('PN', True) + "failed. Please confirm!")
    return False

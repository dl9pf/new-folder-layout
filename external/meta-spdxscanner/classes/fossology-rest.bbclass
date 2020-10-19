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

#upload OSS into No.1 folder of fossology
FOLDER_ID = "1"

HOSTTOOLS_NONFATAL += "curl"

CREATOR_TOOL = "fossology-rest.bbclass in meta-spdxscanner"

NO_PROXY ?= "127.0.0.1"

# If ${S} isn't actually the top-level source directory, set SPDX_S to point at
# the real top-level directory.
SPDX_S ?= "${S}"

python do_spdx () {
    import os, sys, shutil

    pn = d.getVar('PN')
    assume_provided = (d.getVar("ASSUME_PROVIDED") or "").split()
    if pn in assume_provided:
        for p in d.getVar("PROVIDES").split():
            if p != pn:
                pn = p
                break
    if d.getVar('BPN') in ['gcc', 'libgcc']:
        bb.debug(1, 'spdx: There is bug in scan of %s is, do nothing' % pn)
        return
    # The following: do_fetch, do_unpack and do_patch tasks have been deleted,
    # so avoid archiving do_spdx here.
    if pn.startswith('glibc-locale'):
        return
    if (d.getVar('PN') == "libtool-cross"):
        return
    if (d.getVar('PN') == "libgcc-initial"):
        return
    if (d.getVar('PN') == "shadow-sysroot"):
        return

    spdx_outdir = d.getVar('SPDX_OUTDIR')
    spdx_workdir = d.getVar('SPDX_WORKDIR')
    spdx_temp_dir = os.path.join(spdx_workdir, "temp")
    temp_dir = os.path.join(d.getVar('WORKDIR'), "temp")
    
    info = {} 
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
    info['token'] = (d.getVar('TOKEN', True) or "")
    
    srcuri = d.getVar("SRC_URI", False).split()
    length = len("file://")
    for item in srcuri:
        if item.startswith("file://"):
            item = item[length:]
            if item.endswith(".patch") or item.endswith(".diff"):
                info['modified'] = "true"

    manifest_dir = (d.getVar('SPDX_DEPLOY_DIR', True) or "")
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
        shutil.rmtree(spdx_temp_dir)
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
    bb.note(' run fossology rest api ...... ')
    if not os.path.isfile(tar_name):
        bb.warn(info['pn'] + "has no source, do nothing")
        return
    folder_id = get_folder_id(d)
    if invoke_rest_api(d, tar_name, sstatefile, folder_id) == False:
        bb.warn(info['pn'] + ": Get spdx file fail, please check fossology server.")
        remove_file(tar_name)
        return False
    if get_cached_spdx(sstatefile) != None:
        write_cached_spdx( info,sstatefile,cur_ver_code )
        ## CREATE MANIFEST(write to outfile )
        create_manifest(info,sstatefile)
    else:
        bb.warn(info['pn'] + ': Can\'t get the spdx file ' + '. Please check fossology server.')
    remove_file(tar_name)
}

def get_folder_id_by_name(d, folder_name):
    import os
    import subprocess
    import json

    no_proxy = (d.getVar('NO_PROXY', True) or "")

    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting TOKEN!\n" + srcPath)
        raise OSError(errno.ENOENT, "No setting of TOKEN comes from fossology server.")

    rest_api_cmd = "curl -k -s -S -X GET " + server_url + "/api/v1/folders" \
                   + " -H \"Authorization: Bearer " + token + "\"" \
                   + " --noproxy " + no_proxy
    bb.note("Invoke rest_api_cmd = " + rest_api_cmd )
    try:
        all_folder = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
    except subprocess.CalledProcessError as e:
        bb.error(d.getVar('PN', True) + ": Get folder list failed: \n%s" % e.output.decode("utf-8"))
        return False
    all_folder = str(all_folder, encoding = "utf-8")
    bb.note("all_folder list= " + all_folder)
    all_folder = json.loads(all_folder)
    bb.note("len of all_folder = ")
    bb.note(str(len(all_folder)))
    if len(all_folder) == 0:
        bb.note("Can not get folder list.")
        return False
    bb.note("all_folder[0][name] = ")
    bb.note(all_folder[0]["name"])
    for i in range(0, len(all_folder)):
        if all_folder[i]["name"] == folder_name:
                bb.note("Find " + folder_name + "in fossology server ")
                return all_folder[i]["id"]
    return False

def create_folder(d, folder_name):
    import os
    import subprocess

    no_proxy = (d.getVar('NO_PROXY', True) or "")
    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting TOKEN!\n" + srcPath)
        raise OSError(errno.ENOENT, "No setting of TOKEN comes from fossology server.")

    rest_api_cmd = "curl -k -s -S -X POST " + server_url + "/api/v1/folders" \
                   + " -H \'parentFolder: 1\'" \
                   + " -H \'folderName: " + folder_name + "\'" \
                   + " -H \"Authorization: Bearer " + token + "\"" \
                   + " --noproxy " + no_proxy
    bb.note("Invoke rest_api_cmd = " + rest_api_cmd)
    try:
        add_folder = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
    except subprocess.CalledProcessError as e:
        bb.error(d.getVar('PN', True) + ": Added folder failed: \n%s" % e.output.decode("utf-8"))
        return False

    add_folder = str(add_folder, encoding = "utf-8")
    bb.note("add_folder = ")
    bb.note(add_folder)
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

    if d.getVar('FOLDER_NAME', False):
        folder_name = d.getVar('FOLDER_NAME')
        folder_id = create_folder(d, folder_name)
    else:
        folder_id = (d.getVar('FOLDER_ID', True) or "1")

    bb.note("Folder Id =  " + str(folder_id))
    return str(folder_id)

def has_upload(d, tar_file, folder_id):
    import os
    import subprocess
    
    (work_dir, file_name) = os.path.split(tar_file) 
    no_proxy = (d.getVar('NO_PROXY', True) or "")
    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting TOKEN!\n" + srcPath)
        raise OSError(errno.ENOENT, "No setting of TOKEN comes from fossology server.")

    rest_api_cmd = "curl -k -s -S -X GET " + server_url + "/api/v1/uploads" \
                   + " -H \"Authorization: Bearer " + token + "\"" \
                   + " --noproxy " + no_proxy
    bb.note("Invoke rest_api_cmd = " + rest_api_cmd )
        
    try:
        upload_output = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
    except subprocess.CalledProcessError as e:
        bb.error("curl failed: \n%s" % e.output.decode("utf-8"))
        return False

    upload_output = str(upload_output, encoding = "utf-8")
    upload_output = eval(upload_output)
    bb.note("upload_output = ")
    print(upload_output)
    bb.note("len of upload_output = ")
    bb.note(str(len(upload_output)))
    if len(upload_output) == 0:
        bb.note("The upload of fossology is 0.")
        return False
    bb.note("upload_output[0][uploadname] = ")
    bb.note(upload_output[0]["uploadname"])
    bb.note("len of upload_output = ")
    bb.note(str(len(upload_output)))
    for i in range(0, len(upload_output)):
        if upload_output[i]["uploadname"] == file_name and str(upload_output[i]["folderid"]) == str(folder_id):
            bb.warn("Find " + file_name + " in fossology server. So, will not upload again.")
            return upload_output[i]["id"]
    return False

def upload(d, tar_file, folder):
    import os
    import subprocess
    delaytime = 50
    i = 0
 
    no_proxy = (d.getVar('NO_PROXY', True) or "")
    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting TOKEN!\n" + srcPath)
        raise OSError(errno.ENOENT, "No setting of TOKEN comes from fossology server.")
    
    rest_api_cmd = "curl -k -s -S -X POST " + server_url + "/api/v1/uploads" \ 
                    + " -H \"folderId: " + folder + "\"" \
                    + " -H \"Authorization: Bearer " + token + "\"" \
                    + " -H \'uploadDescription: created by REST\'" \
                    + " -H \'public: public\'"  \
                    + " -H \'Content-Type: multipart/form-data\'"  \
                    + " -F \'fileInput=@\"" + tar_file + "\";type=application/octet-stream\'" \
                    + " --noproxy " + no_proxy
    bb.note("Upload : Invoke rest_api_cmd = " + rest_api_cmd )
    while i < 10:
        time.sleep(delaytime)
        try:
            upload = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
        except subprocess.CalledProcessError as e:
            bb.error(d.getVar('PN', True) + ": Upload failed: \n%s" % e.output.decode("utf-8"))
            return False
        upload = str(upload, encoding = "utf-8")
        bb.note("Upload = ")
        bb.note(upload)
        upload = eval(upload)
        if str(upload["code"]) == "201":
            return upload["message"]
        i += 1
    bb.warn(d.getVar('PN', True) + ": Upload is fail, please check your fossology server.")
    return False

def analysis(d, folder_id, upload_id):
    import os
    import subprocess
    delaytime = 50
    i = 0

    no_proxy = (d.getVar('NO_PROXY', True) or "")
    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting TOKEN!\n" + srcPath)
        raise OSError(errno.ENOENT, "No setting of TOKEN comes from fossology server.")

    rest_api_cmd = "curl -k -s -S -X POST " + server_url + "/api/v1/jobs" \
                    + " -H \"folderId: " + str(folder_id) + "\"" \
                    + " -H \"uploadId: " + str(upload_id) + "\"" \
                    + " -H \"Authorization: Bearer " + token + "\"" \
                    + " -H \'Content-Type: application/json\'" \
                    + " --data \'{\"analysis\": {\"bucket\": true,\"copyright_email_author\": true,\"ecc\": true, \"keyword\": true,\"mime\": true,\"monk\": true,\"nomos\": true,\"package\": true},\"decider\": {\"nomos_monk\": true,\"bulk_reused\": true,\"new_scanner\": true}}\'" \
                    + " --noproxy " + no_proxy
    bb.note("Analysis : Invoke rest_api_cmd = " + rest_api_cmd )
    while i < 10:
        try:
            time.sleep(delaytime)
            analysis = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
        except subprocess.CalledProcessError as e:
            bb.error("Analysis failed: \n%s" % e.output.decode("utf-8"))
            return False
        time.sleep(delaytime)
        analysis = str(analysis, encoding = "utf-8")
        bb.note("analysis  = ")
        bb.note(analysis)
        analysis = eval(analysis)
        if str(analysis["code"]) == "201":
            return analysis["message"]
        elif str(analysis["code"]) == "404":
            bb.warn(d.getVar('PN', True) + ": analysis is still not complete.")
            time.sleep(delaytime*2)
        else:
            return False
        i += 1
        bb.warn(d.getVar('PN', True) + ": Analysis is fail, will try again.")
    bb.warn(d.getVar('PN', True) + ": Analysis is fail, please check your fossology server.")
    return False

def trigger(d, folder_id, upload_id):
    import os
    import subprocess
    delaytime = 50
    i = 0

    no_proxy = (d.getVar('NO_PROXY', True) or "")
    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting TOKEN!\n" + srcPath)
        raise OSError(errno.ENOENT, "No setting of TOKEN comes from fossology server.")

    rest_api_cmd = "curl -k -s -S -X GET " + server_url + "/api/v1/report" \
                    + " -H \"Authorization: Bearer " + token + "\"" \
                    + " -H \"uploadId: " + str(upload_id) + "\"" \
                    + " -H \'reportFormat: spdx2tv\'" \
                    + " --noproxy " + no_proxy
    bb.note("trigger : Invoke rest_api_cmd = " + rest_api_cmd )
    while i < 10:
        time.sleep(delaytime)
        try:
            trigger = subprocess.check_output(rest_api_cmd, stderr=subprocess.STDOUT, shell=True)
        except subprocess.CalledProcessError as e:
            bb.error(d.getVar('PN', True) + ": Trigger failed: \n%s" % e.output.decode("utf-8"))
            return False
        time.sleep(delaytime)
        trigger = str(trigger, encoding = "utf-8")
        trigger = eval(trigger)
        bb.note("trigger id = ")
        bb.note(str(trigger["message"]))
        if str(trigger["code"]) == "201":
            return trigger["message"].split("/")[-1]
        i += 1
        time.sleep(delaytime * 2)
        bb.warn(d.getVar('PN', True) + ": Trigger is fail, will try again.")
    bb.warn(d.getVar('PN', True) + ": Trigger is fail, please check your fossology server.")
    return False

def get_spdx(d, report_id, spdx_file):
    import os
    import subprocess
    import time
    delaytime = 50
    complete = False
    i = 0

    no_proxy = (d.getVar('NO_PROXY', True) or "")
    server_url = (d.getVar('FOSSOLOGY_SERVER', True) or "")
    if server_url == "":
        bb.note("Please set fossology server URL by setting FOSSOLOGY_SERVER!\n")
        raise OSError(errno.ENOENT, "No setting of  FOSSOLOGY_SERVER")

    token = (d.getVar('TOKEN', True) or "")
    if token == "":
        bb.note("Please set token of fossology server by setting TOKEN!\n" + srcPath)
        raise OSError(errno.ENOENT, "No setting of TOKEN comes from fossology server.")
    rest_api_cmd = "curl -k -s -S -X GET " + server_url + "/api/v1/report/" + report_id \
                    + " -H \'accept: text/plain\'" \
                    + " -H \"Authorization: Bearer " + token + "\"" \
                    + " --noproxy " + no_proxy 
    bb.note("get_spdx : Invoke rest_api_cmd = " + rest_api_cmd )
    while i < 10:
        time.sleep(delaytime)
        file = open(spdx_file,'wt')
        try:
            p = subprocess.Popen(rest_api_cmd, shell=True, universal_newlines=True, stdout=file).wait()
        except subprocess.CalledProcessError as e:
            bb.error("Get spdx failed: \n%s. Please check fossology server." % e.output.decode("utf-8"))
            file.close()
            os.remove(spdx_file)
            return False
        file.flush()
        time.sleep(delaytime)
        file.close()
        file = open(spdx_file,'r+')
        first_line = file.readline()
        if "SPDXVersion" in first_line:
            line = file.readline()
            while line:
                if "LicenseID:" in line:
                    complete = True
                    break
                line = file.readline()
            file.close()
            if complete == False:
                bb.warn("license info not complete, try agin.")
            else:
                return True
        else:
            bb.warn(d.getVar('PN', True) + ": Get the first line is " + first_line + ". Try agin")
            os.remove(spdx_file)

        file.close()
        i += 1
        delaytime = delaytime + 20
        time.sleep(delaytime)

    file.close()
    bb.warn(d.getVar('PN', True) + ": SPDX file maybe have something wrong, please confirm.")

def invoke_rest_api(d, tar_file, spdx_file, folder_id):
    import os
    import time
    i = 0
        
    bb.note("invoke fossology REST API : tar_file = %s " % tar_file)
    upload_id = has_upload(d, tar_file, folder_id)
    if upload_id == False:
        bb.note("This OSS has not been scanned. So upload it to fossology server.")
        upload_id = upload(d, tar_file, folder_id)
        if upload_id == False:
            return False
    else:
        report_id = trigger(d, folder_id, upload_id)
        if report_id == False:
            bb.note(d.getVar('PN', True) + ": Although has uploaded,trigger fail. Maybe hasn't analysised.")
        else:
            spdx2tv = get_spdx(d, report_id, spdx_file)
            if spdx2tv == False:
                bb.note(d.getVar('PN', True) + ": Although has uploaded,get report fail. Maybe hasn't analysised.")
            else:
                return True
    
    if analysis(d, folder_id, upload_id) == False:
        return False
    while i < 10:
        i += 1
        report_id = trigger(d, folder_id, upload_id)
        if report_id == False:
            return False
        spdx2tv = get_spdx(d, report_id, spdx_file)
        if spdx2tv == False:
            bb.warn(d.getVar('PN', True) + ": get_spdx is unnormal. Will try again!")
        else:
            return True

    bb.warn("get_spdx of %s is unnormal. Please confirm!")
    return False

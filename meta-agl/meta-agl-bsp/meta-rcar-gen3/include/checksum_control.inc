require checksum_control_files.inc

python do_checksum_control() {
    to_check_dico = d.getVarFlags("CTL_CHECKSUM")
    if to_check_dico is None:
        return 0

    src_uri = (d.getVar('SRC_URI') or "").split()
    for s in src_uri:
        c_source=os.path.basename(s)
        bb.note("Check source: %s " % (c_source))
        if c_source in to_check_dico:
            f_data = bb.fetch2.FetchData(s, d, True)
            f_data.setup_localpath(d)
            if os.path.exists(f_data.localpath):
                mdr5_sum=bb.utils.md5_file(f_data.localpath)
                mdr5_ctl=to_check_dico[c_source]
                if mdr5_sum != mdr5_ctl:
                    bb.fatal( "File %s hash should be %s but %s find, please update your driver files." % (c_source, mdr5_ctl, mdr5_sum))
}

do_checksum_control[doc] = "Do a checksum of the archives source files"
do_checksum_control[vardeps] = "CTL_CHECKSUM"

addtask do_checksum_control before do_unpack after do_fetch

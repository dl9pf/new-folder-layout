# (C) 2017 Jan-Simon Möller <dl9pf@gmx.de>
#
# License: GPLv2
#
# summary with
# cloc --sum-reports `find . -name "*clocreport" | grep -v "\-native" ` --out summary.report

# cloc needs to be installed on the host
# FIXME: create a cloc-native
HOSTTOOLS += "cloc"

CLOC_DIRECTORY ??= "${DEPLOY_DIR}/cloc/"
CLOCSTATEDIR = "${WORKDIR}/cloc-destdir/"

python do_cloc() {

    import subprocess

    source_dir = d.getVar('S', True)
    pn = d.getVar('PN', True)

    destdir = d.expand('${CLOCSTATEDIR}')
    bb.utils.mkdirhier(destdir)

    cloc_cmd = 'cloc %s -q --out=%s/%s.clocreport' % (source_dir, destdir, pn )
    subprocess.call(cloc_cmd, shell=True)

}


addtask cloc before do_configure after do_patch

EXPORT_FUNCTIONS do_cloc

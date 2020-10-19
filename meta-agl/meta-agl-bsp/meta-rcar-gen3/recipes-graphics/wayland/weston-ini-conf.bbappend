FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_ulcb = " file://kingfisher_output.cfg"
SRC_URI_append_ebisu = " file://ebisu_output.cfg"
SRC_URI_append_salvator-x = " file://salvator-x_output.cfg"

do_configure() {
    echo repaint-window=34 >> ${WORKDIR}/core.cfg

    echo transition-duration=300 >> ${WORKDIR}/ivishell.cfg
    echo cursor-theme=default >> ${WORKDIR}/ivishell.cfg
}

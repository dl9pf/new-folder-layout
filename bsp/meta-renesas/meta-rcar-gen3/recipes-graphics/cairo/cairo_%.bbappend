require include/gles-control.inc

DEPENDS_append_rcar-gen3 = "${@ ' gles-user-module' if '${USE_GLES_WAYLAND}' == '1' else ''}"

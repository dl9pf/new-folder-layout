# The gallium-llvm is recommended as software 3D graphics renderer
GALLIUM_LLVM = "gallium-llvm"
PACKAGECONFIG_append_qemux86 = " gallium ${GALLIUM_LLVM}"
PACKAGECONFIG_append_qemux86-64 = " gallium ${GALLIUM_LLVM}"
PACKAGECONFIG_append_intel-corei7-64 = " gallium ${GALLIUM_LLVM}"

DRIDRIVERS_append_intel-corei7-64 = ",i965"

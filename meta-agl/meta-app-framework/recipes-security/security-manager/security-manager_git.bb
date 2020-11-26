require security-manager.inc

PV = "1.0.2+git${SRCPV}"
SRCREV = "860305a595d681d650024ad07b3b0977e1fcb0a6"
SRC_URI += "git://github.com/Samsung/security-manager.git"
S = "${WORKDIR}/git"

SRC_URI += " \
   file://0001-systemd-stop-using-compat-libs.patch \
   file://0002-security-manager-policy-reload-do-not-depend-on-GNU-.patch \
   file://0003-Smack-rules-create-two-new-functions.patch \
   file://0004-app-install-implement-multiple-set-of-smack-rules.patch \
   file://0005-c-11-replace-deprecated-auto_ptr.patch \
   file://0006-socket-manager-removes-tizen-specific-call.patch \
   file://0007-removes-dependency-to-libslp-db-utils.patch \
   file://0008-Fix-gcc6-build.patch \
   file://0009-Fix-Cmake-conf-for-gcc6-build.patch \
   file://0010-gcc-7-requires-include-functional-for-std-function.patch \
   file://0011-Fix-gcc8-warning-error-Werror-catch-value.patch \
   file://0012-Avoid-casting-from-const-T-to-void.patch \
   file://0013-Removing-tizen-platform-config.patch \
   file://0014-Ensure-post-install-initialization-of-database.patch \
   file://0015-Restrict-socket-accesses.patch \
"

# Use make with cmake and not ninja
OECMAKE_GENERATOR = "Unix Makefiles"

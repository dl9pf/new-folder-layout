require conf/include/agl_rcar-nogfx.inc

# Enable AGL virtualization features
MACHINE_FEATURES_append = " agl-egvirt"

# Enable Gfx Pkgs
MACHINE_FEATURES_append = " gsx"
MULTI_PROVIDER_WHITELIST += "virtual/libgl virtual/egl virtual/libgles1 virtual/libgles2"

# for Wayland/Weston
PREFERRED_PROVIDER_virtual/libgles1 = ""
PREFERRED_PROVIDER_virtual/libgles2 = "virtual-gles-user-module"
PREFERRED_PROVIDER_virtual/egl = "virtual-gles-user-module"
PREFERRED_PROVIDER_virtual/libgl = ""
PREFERRED_PROVIDER_virtual/mesa = "mesa"
PREFERRED_PROVIDER_libgbm = "libgbm"
PREFERRED_RPROVIDER_libgbm-dev = "libgbm"

PREFERRED_RPROVIDER_libomxil = "omx-user-module"
PREFERRED_PROVIDER_virtual/libomxil = "omx-user-module"

# Add for gstreamer plugins ugly
LICENSE_FLAGS_WHITELIST = "commercial"

MACHINE_FEATURES_append = " multimedia"

DISTRO_FEATURES_append = " use_eva_pkg"

#DISTRO_FEATURES_append = " h265dec_lib mpeg2dec_lib"
DISTRO_FEATURES_append = " h264dec_lib h264enc_lib aaclcdec_lib aaclcdec_mdw"

# Mask out meta-renesas-rcar-gen3's tweaks to weston-init, as they do
# not seem necessary for anything in AGL, and the addition of
# dbus.service and multi-user.target dependencies to the weston systemd
# unit that is done is problematic with respect to potentially blocking
# weston if any user units like application framework "platform"
# services fail. They also likely affect boot up speed with respect to
# getting weston started.  There is no clear rationale expressed in the
# meta-renesas-rcar-gen3 commit history for the extra dependencies, and
# testing shows no issues on H3ULCB when not having them.
BBMASK += "meta-rcar-gen3/recipes-graphics/wayland/weston-init.bbappend"

IMAGE_INSTALL_append_rcar-gen3 = " \
    kernel-devicetree \
    kernel-module-pvrsrvkm \
    kernel-module-vsp2 \
    kernel-module-vspm  \
    kernel-module-vspm-if \
    omx-user-module \
"

# Needed for wayland-egl"
PACKAGECONFIG[wayland-gles2] = ",,mesa"

# Use GLES2 instead of GL
PACKAGECONFIG_remove = "wayland-gl drm-gl x11-gl"

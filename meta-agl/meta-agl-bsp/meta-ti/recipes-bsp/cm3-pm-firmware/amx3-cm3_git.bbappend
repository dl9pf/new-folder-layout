# We need to override some of the build in defaults and the recipe is very
# strict in how it grabs the correct compiler.  Pass in our override here.
TOOLCHAIN_OPTIONS += "${SECURITY_NOPIE_CFLAGS}"

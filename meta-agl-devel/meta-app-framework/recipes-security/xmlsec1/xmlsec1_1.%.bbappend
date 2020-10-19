# Disable nss to avoid build issues on native
PACKAGECONFIG = "gnutls libgcrypt openssl des"

BBCLASSEXTEND = "native nativesdk"

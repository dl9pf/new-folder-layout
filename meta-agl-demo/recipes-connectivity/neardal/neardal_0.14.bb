require neardal.inc

SRC_URI = "git://github.com/connectivity/neardal.git;protocol=https \
	file://ncl.patch \
	file://0001-neardal-ncl-fix-segfault-on-help-page-being-displaye.patch	\
	file://0002-neardal-lib-fix-memory-corruption.patch		\
	"
SRCREV = "fe0fa79c94e9a0f1c2cfa1f58b3acc9bdc7d5e13"

S = "${WORKDIR}/git"

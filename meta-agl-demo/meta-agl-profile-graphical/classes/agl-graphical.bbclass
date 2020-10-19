WESTONUSER ??= "display"
WESTONGROUP ??= "display"
WESTONARGS ?= "--idle-time=0  --tty=7"
WESTONLAUNCHARGS ??= "--tty /dev/tty7 --user ${WESTONUSER}"
DISPLAY_XDG_RUNTIME_DIR ??= "/run/platform/${WESTONUSER}"


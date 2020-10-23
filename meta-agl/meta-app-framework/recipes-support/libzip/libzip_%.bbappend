require ${@bb.utils.contains('DISTRO_FEATURES', 'appfw', '${BPN}_appfw.inc', '', d)}

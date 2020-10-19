require ${@bb.utils.contains('AGL_FEATURES', 'aglcore', '${BPN}_aglcore.inc', '', d)}

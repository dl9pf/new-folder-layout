require ${@bb.utils.contains('NETBOOT_ENABLED', '1', '${BPN}_netboot.inc', '', d)}

do_configure_append () {
	sed -i 's/state.rcarsound\ {/state.ak4613\ {/g' ${WORKDIR}/asound.state
}

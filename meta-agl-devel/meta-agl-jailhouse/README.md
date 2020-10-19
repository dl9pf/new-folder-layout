# Jailhouse support layer

Yocto layer that enables use of the Jailhouse partitioning hypervisor - <https://github.com/siemens/jailhouse>.

## How to use

The AGL feature `agl-jailhouse` has to be enabled. That needs to be done when including aglsetup.sh, for example:

    source meta-agl/scripts/aglsetup.sh -m raspberrypi4 agl-demo agl-netboot agl-appfw-smack agl-jailhouse

That will enable this layer and include the `jailhouse` package in the image.

Then, in the target system, the cell configurations (*.cell) are placed in `/usr/share/jailhouse/cells/` and the demo inmates (bare-metal applications to run in a non-root cell) are located in `/usr/share/jailhouse/inmates`.

## Raspberry Pi 4 example

Use this commands to enable Jailhouse and run the GIC demo inmate in a non-root cell. After issuing these commands, the GIC demo will be mesauring jitter of a timer and print the output on the serial console of the RPi.

    jailhouse enable /usr/share/jailhouse/cells/rpi4.cell
    jailhouse cell create /usr/share/jailhouse/cells/rpi4-inmate-demo.cell
    jailhouse cell load inmate-demo /usr/share/jailhouse/inmates/gic-demo.bin
    jailhouse cell start inmate-demo

## Dependencies

This layer depends on:

* URI: git://git.yoctoproject.org/meta-arm
  * branch: dunfell
  * revision: 0bd9c740267c0926e89bcfdb489790b7bf1fbd4b
  * note: actually only required on the Raspberry Pi 4 target

## Supported targets

* Raspberry Pi 4
    * All (1G-8G) memory variants. But note that there is 256M reserved for Jailhouse and 256MiB for GPU in AGL, so the smaller variants are not recommended.

* QEMU x86-64
    * Work in progress. Requires KVM. Nested virtualization must be enabled on the host. Currently, the right configuration of QEMU and Jailhouse to work out-of-box is being worked on.





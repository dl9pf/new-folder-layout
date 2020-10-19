---
description: machine ebisu
author: <undefined>
---
## Machine 'ebisu'

### Prepare the build

Before building the AGL distribution for Renesas 'ebisu' board, it is necessary to prepare the environnement. See [AGL Doc - Initializing Your Build Environment](https://docs.automotivelinux.org/docs/en/master/getting_started/reference/getting-started/image-workflow-initialize-build-environment.html) before going to the next step.

The 'ebisu' board need some specifics binaries in order to build. These binaries contain Graphics support, specific drivers... They are only delivered by Renesas.
Before setting up the build with `meta-agl/scripts/aglsetup.sh`, the environment variable `EBISU_BIN_PATH` need to be set. This variable specify the path to a folder which contains all ebisu's binaries zip files.

Moreover, it's possible to launch a custom bash script during the setup. This optional step will be called at the end of the `aglsetup.sh` sequence. This can be used to add a specific configuration to the official setup or add a hotfix.

Example:
```bash
$ cd $AGL_TOP
$ export EBISU_BIN_PATH=/home/user/Downloads/ebisu_binaries
$ export CUSTOM_RENESAS_CONFIG_SCRIPT=/path/to/my/Renesas_custom_setup_ebisu.sh #optional
$ source meta-agl/scripts/aglsetup.sh -m ebisu agl-demo -f
```

### Launch the build

When your environnment is ready, you can launch the AGL build with `bitbake` :

```bash
$ cd $AGL_TOP
$ bitbake agl-demo-platform
```

### Debugging the board

To debug the ebisu board, the PC should be connected to the CN25 USB serial port.
And the serial communication's protocol of the terminal software on the PC should be set as follows:

Parameter | Value
---      | ---
Transfer rate | 115200 bps
Data length | 8 bits
Parity | Not in use
Stop bit | 1 bit
Flow control | Not provided

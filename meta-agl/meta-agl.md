## Introduction

The `meta-agl` layer provides the minimal set of software
to boot an AGL Distribution system.
You use this layer as the minimal core on which to build AGL profiles.

**NOTE:** The `meta-agl` layer does not include a reference UI.
  The reference UI is included as part of the
  [`meta-agl-demo`](./meta-agl-demo.html) layer.
  Furthermore, `meta-agl` does not include additional components, such
  as security, which are part of the
  `meta-agl-extra` layer.

## Sub-Layers

The `meta-agl` layer itself contains many sub-layers and files.
Following is a "tree" look at the layer:

```
.
├── docs
├── meta-agl-core
├── meta-agl-bsp
├── meta-agl-distro
├── meta-netboot
├── meta-pipewire
├── README-AGL.md
├── README.md
├── scripts
├── templates
```

This list provides some overview information on the files and sub-layers
in `meta-agl`:

* `docs`: Contains files that support AGL documentation.
* `meta-agl-core`: Contains layer configuration for the `meta-agl` layer.
* `meta-agl-bsp`: Contains adaptations for recipes and required packages
  to boot an AGL distribution on targeted hardware and emulation (i.e. QEMU).
* `meta-agl-distro`: Contains distro configuration and supporting scripts.
* `meta-netboot`: Contains recipes and configuration adjustments to allow network
  boot through network block device (NBD) since network file system (NFS) does not
  support security labels.
* `meta-pipewire`: Pipewire support and wireplumber
* `scripts`: AGL development setup and support scripts.
* `templates`: Base, feature, and machine templates used in the AGL development
  environment.

## Packagegroups

This section describes the AGL
[packagegroup](https://yoctoproject.org/docs/3.1.2/dev-manual/dev-manual.html#usingpoky-extend-customimage-customtasks)
design:

tbd

* packagegroup-agl-image-minimal

* packagegroup-agl-image-ivi


## Introduction

The `meta-agl-demo` layer is the reference user interface layer for the DEMO
platform of Automotive Grade Linux (AGL).
The layer provides a reference platform and applications.
The BitBake target name for the DEMO platform is `agl-demo-platform`, which is
the full DEMO platform image.

## Layer Dependencies

This section describes dependencies for the `meta-agl-demo` layer.
Dependencies are grouped into base, hardware, and feature dependencies.

### Base Dependencies

The `meta-agl-demo` layer has the following base dependencies:

* Yocto Project Release:

  - URI: git://git.yoctoproject.org/poky
  - Branch: "dunfell"
  - Tested Revision: See the [`default.xml`](https://github.com/leon-anavi/AGL-repo/blob/master/default.xml)
    manifest file for the `AGL-repo` repository for revision
    information.<br/><br/>

* AGL `meta-agl` Layer:

  - URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl
  - Branch: "master"<br/><br/>

* OpenEmbedded `meta-openembedded` Layer:

  - Branch: "thud"
  - Tested Revision: See the [`default.xml`](https://github.com/leon-anavi/AGL-repo/blob/master/default.xml)
    manifest file for the `AGL-repo` repository for revision
    information.

    Specifically, out of `meta-openembedded`, these sub-layers are used:

    - `meta-oe`
    - `meta-multimedia`
    - `meta-networking`
    - `meta-python`<br/><br/>

* Yocto Project `meta-qt5` Layer from the
  [OpenEmbedded Layer Index](https://layers.openembedded.org/layerindex/branch/master/layers/):

  - URI: https://github.com/meta-qt5/meta-qt5.git
  - Branch:   "dunfell"
  - Tested Revision: See the [`default.xml`](https://github.com/leon-anavi/AGL-repo/blob/master/default.xml)
    manifest file for the `AGL-repo` repository for revision
    information.<br/><br/>


### Feature Dependencies

The `meta-agl-demo` layer has the following AGL
[feature](../getting_started/reference/getting-started/image-workflow-initialize-build-environment.html#agl-features)
dependencies:

* Yocto Project `meta-security` Layer:

  - URI: https://git.yoctoproject.org/cgit/cgit.cgi/meta-security
  - Branch: "master"
  - Tested Revision: See the [`default.xml`](https://github.com/leon-anavi/AGL-repo/blob/master/default.xml)
    manifest file for the `AGL-repo` repository for revision
    information.<br/><br/>

* AGL's `meta-app-framework` Layer within the `meta-agl` Layer:

  - URI: https://gerrit.automotivelinux.org/gerrit/gitweb?p=AGL/meta-agl.git
  - Branch: "master"<br/><br/>

**The `agl-sota` Feature:**

* Here Technologies' `meta-updater` Layer:

  - URI: https://github.com/advancedtelematic/meta-updater/
  - Branch: "dunfell"<br/><br/>

* Here Technologies' `meta-updater-qemux86-64` Layer:

  - URI: https://github.com/advancedtelematic/meta-updater-qemux86-64/
  - Branch: "dunfell"<br/><br/>

* OpenEmbedded's `meta-openembedded` Layer:

  - URI: https://github.com/openembedded/meta-openembedded
  - Branch: "dunfell"
  - Tested Revision: See the [`default.xml`](https://github.com/leon-anavi/AGL-repo/blob/master/default.xml)
    manifest file for the `AGL-repo` repository for revision
    information.

    Specifically, out of `meta-openembedded`, these sub-layers are used:

    - `meta-filesystems`
    - `meta-oe`
    - `meta-python`<br/><br/>

**The `agl-netboot` Feature:**

* AGL's `meta-netboot` Layer within the `meta-agl` Layer:

  - URI: https://gerrit.automotivelinux.org/gerrit/gitweb?p=AGL/meta-agl.git
  - Branch: "master"


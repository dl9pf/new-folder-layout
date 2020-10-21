# AGL Profiles

The AGL Profiles cover the different use-cases that the AGL platform serves.\
This ranges from minimal systems without display, telematic systems, HUD, IVI, ADAS and more.\
Common to all of them is the use of the AGL APIs (agl-service-*).

All systems have the 'core' profile in common.\
This small set of libraries and binaries is the essential set including the AGL APIs that every AGL system needs.\
All other profiles reuse the 'core' profile.

The other profiles and their dependencies are currently:

* agl-core
  * agl-demo-platform
  * agl-profile-telematics
  * agl-profile-hud

## agl-profile-core

This profile contains the following images:

* agl-image-boot
  * agl-image-minimal
  * agl-image-minimal-crosssdk

### agl-image-boot

This image is only meant to boot the target device and provide network, package-management and a shell.\
All other functionality needs to be installed at runtime through 'dnf' using the provided package feeds and package-groups (e.g. dnf install profile-graphical-qt5)

### agl-image-minimal

This is the smallest image that includes all (non-graphical) AGL APIs.

### agl-image-minimal-crosssdk

This is the SDK for systems without display including the AGL APIs.

## agl-profile-telematics

N.N.

## agl-demo-platform

This layer builds on-top of agl-profile-graphical-qt5 and builds the full AGL demonstrator image.\
It hosts these images:

* agl-demo-platform
* agl-demo-platform-crosssdk
* agl-demo-platform-qa

TLDR:

```tree
meta-agl/meta-agl-profile-core/recipes-platform
|-- images
|   |-- agl-image-boot.bb
|   |-- agl-image-boot.inc
|   |-- agl-image-minimal-crosssdk.bb
|   |-- agl-image-minimal-qa.bb
|   |-- agl-image-minimal.bb
|   `-- agl-image-minimal.inc
`-- packagegroups
    |-- packagegroup-agl-core-boot.bb
    |-- packagegroup-agl-core-connectivity.bb
    |-- packagegroup-agl-core-multimedia.bb
    |-- packagegroup-agl-core-navigation.bb
    |-- packagegroup-agl-core-os-commonlibs.bb
    |-- packagegroup-agl-core-security.bb
    |-- packagegroup-agl-core-services.bb
    |-- packagegroup-agl-image-boot.bb
    `-- packagegroup-agl-image-minimal.bb

    meta-agl/meta-agl-profile-graphical/recipes-platform
|-- images
|   |-- agl-image-weston.bb
|   `-- agl-image-weston.inc
`-- packagegroups
    |-- packagegroup-agl-graphical-services.bb
    `-- packagegroup-agl-image-weston.bb

    meta-agl/meta-agl-profile-graphical-html5/recipes-platform
|-- images
|   |-- agl-demo-platform-html5-crosssdk.bb
|   |-- agl-demo-platform-html5.bb
|   `-- agl-demo-platform-html5.inc
`-- packagegroups
    `-- packagegroup-agl-demo-platform-html5.bb

    meta-agl/meta-agl-profile-graphical-qt5/recipes-platform
|-- images
|   |-- agl-image-graphical-qt5-crosssdk.bb
|   |-- agl-image-graphical-qt5.bb
|   `-- agl-image-graphical-qt5.inc
`-- packagegroups
    |-- packagegroup-agl-appfw-native-qt5.bb
    |-- packagegroup-agl-demo-qt-examples.bb
    |-- packagegroup-agl-profile-graphical-qt5.bb
    `-- packagegroup-qt5-toolchain-target.bbappend
```

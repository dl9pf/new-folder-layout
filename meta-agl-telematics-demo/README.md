**README.md for the 'meta-agl-telematics-demo' layer.**

**See README-AGL.md in meta-agl for general information about Automotive Grade Linux.**


meta-agl-telematics-demo, the layer for the Telematics DEMO platform of Automotive Grade Linux
=================================================================================

The layer 'meta-agl-telematics-demo' provides a reference/demo platform and
applications for the AGL Distribution.

AGL is creating an automotive specific Linux distribution (AGL UCB) that unifies
the software that has been written in a number of places already,
such as GENIVI and Tizen IVI.

The AGL community appreciates feedback, ideas, suggestion, bugs and
documentation just as much as code. Please join the irc conversation
at the #automotive channel on irc.freenode.net and our mailing list.

For infomation for subscribing to the mailing list
    [agl-dev-community](https://lists.automotivelinux.org/g/agl-dev-community)
For information about AGL Distribution, see the
    [AGL Distribution](https://wiki.automotivelinux.org/agl-distro)
For information abount Getting started with AGL
    [here](https://wiki.automotivelinux.org/start/getting-started)
For information about contributing to the AGL Distro
    [here](https://wiki.automotivelinux.org/agl-distro/contributing)


Quick start guide
-----------------
See README-AGL.md in meta-agl


Layer Dependencies
------------------

* Base dependencies [agl-telematics-demo]:

URI: git://git.yoctoproject.org/poky

URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl

URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl-devel

URI: git://git.openembedded.org/meta-openembedded

Specifically out of meta-openembedded these sub-layers are used:

 - meta-openembedded/meta-oe
 - meta-openembedded/meta-multimedia
 - meta-openembedded/meta-efl
 - meta-openembedded/meta-networking
 - meta-openembedded/meta-python
 - meta-openembedded/meta-ruby

* Hardware dependencies:

The Raspberry Pi 3 board depends in addition on:

URI: git://git.yoctoproject.org/meta-raspberrypi


Packagegroups
-------------

AGL Telematics Demo Platform's package group design:

* packagegroup-agl-telematics-demo-platform

This is for generating the image 'agl-telematics-demo-platform' which is a full
image for the Telematics profile of the AGL distro.

Following meta-agl's design of packagegroups, ``agl-telematics-demo-platform.bb``
contains only ``packagegroup-agl-telematics-demo-platform``.

``packagegroup-agl-telematics-demo-platform`` has one packagegroup in it,
``packagegroup-agl-profile-telematics``, and the packages required for the DEMO
applications.

Supported Machines
------------------

At the moment only the Raspberry Pi 3 has been tested, but the other
AGL supported platforms (see `README-AGL.md` in meta-agl layer), should work.

Supported Target of bitbake
------------------------

* `agl-telematics-demo-platform` is the full image of the AGL Telematics
Demo Platform with all applications


**README.md for the 'meta-agl-cluster-demo' layer.**

**See README-AGL.md in meta-agl for general information about Automotive Grade Linux.**


meta-agl-cluster-demo, the layer for the Instrument Cluster DEMO platform of Automotive Grade Linux
=================================================================================

The layer 'meta-agl-cluster-demo' provides a reference/demo platform and
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

* Base dependencies [agl-cluster-demo]:

URI: git://git.yoctoproject.org/poky

URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl

URI: https://gerrit.automotivelinux.org/gerrit/AGL/meta-agl-devel

URI: git://git.openembedded.org/meta-openembedded

Specifically out of meta-openembedded these sub-layers are used:

 - meta-openembedded/meta-oe
 - meta-openembedded/meta-multimedia
 -  meta-openembedded/meta-efl
 -  meta-openembedded/meta-networking
 -  meta-openembedded/meta-python
 -  meta-openembedded/meta-ruby

URI: https://github.com/meta-qt5/meta-qt5.git

* Hardware dependencies:

The Minnowboard Turbot board depends in addition on:

URI: http://git.yoctoproject.org/meta-intel


Packagegroups
-------------

AGL Cluster Demo Platform's package group design:

* packagegroup-agl-cluster-demo-platform

This is for generating the image 'agl-cluster-demo-platform' which is a full
image for the Instrument Cluster profile of the AGL distro.

Following meta-agl's design of packagegroups, ``agl-cluster-demo-platform.bb``
contains only ``packagegroup-agl-cluster-demo-platform``.

``packagegroup-agl-cluster-demo-platform`` has one packagegroup in it,
``packagegroup-agl-profile-cluster-qt5``, and the packages required for the DEMO
applications.

* packagegroup-agl-cluster-demo-qtcompositor

This is for generating the image 'agl-cluster-demo-qtcompositor'.

Supported Machines
------------------

At the moment only the Minnowboard Turbot has been tested, but the other
AGL supported platforms (see `README-AGL.md` in meta-agl layer), should be
possible to make work by modifying the gstreamer pipeline used in the
agl-cluster-demo-receiver application to replace the use of vaapisink with
an alternative that works for the platform.

Supported Target of bitbake
------------------------

* `agl-cluster-demo-platform` is the full image of the AGL Instrument Cluster
Demo Platform with all applications

* `agl-cluster-demo-qtcompositor` is the image of the AGL Instrument Cluster
Demo that uses qtwayland compositor, contains a simple cluster graphic user
interface and some AGL services.

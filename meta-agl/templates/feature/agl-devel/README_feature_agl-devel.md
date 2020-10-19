---
description: Feature agl-devel
authors: José Bollo <jose.bollo@iot.bzh>, Ronan Le Martret <ronan.lemartret@iot.bzh>, Stephane Desneux <stephane.desneux@iot.bzh>, Yannick Gicquel <yannick.gicquel@iot.bzh>
---
	
### Feature agl-devel
	 
Activation of the agl-devel features turns on
features needed for developping and debugging
agl distribution.

This includes:

* adding to images some useful packages
* adding to images the package group 'packagegroup-agl-devel'
* definition of a contionnal the tag 'agl-devel'
    for conditionnal building

  * definition of the distro feature 'agl-devel'
  * adds packages for development in SDK

### How to use agl-devel in conditionnal builds

The following example shows how to activate C/C++ code
specific to agl-devel:

```yocto
CPPFLAGS_append_agl-devel = " -DAGL_DEVEL"
```

Using this, any code enclosed in

```yocto
#ifdef AGL_DEVEL
...my code specific to agl-devel...
#endif
```

will normaly be effective only if agl-devel is set on.

At this time, it is recommended to use AGL_DEVEL as tag
within C/C++ code.

---
description: Feature agl-voiceagent-alexa-wakeword
authors: Scott Murray <scott.murray@konsulko.com>
---

### Feature agl-voiceagent-alexa-wakeword

Enables building the Amazon Alexa voiceagent binding with included wakeword engine support.

Note that this features assumes that the amazonlite wakeword engine ZIP file (e.g. amazonlite-2.0.zip) containing the required additional files has been decompressed to external/alexa-auto-sdk/extensions/extras/amazonlite in the tree.  Without this, attempting to build with bitbake will fail. Please contact your Amazon Alexa developer account representative if you wish to obtain the wakeword engine.

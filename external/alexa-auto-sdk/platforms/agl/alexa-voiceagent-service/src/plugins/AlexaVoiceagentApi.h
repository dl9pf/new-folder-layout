/*
 * Copyright 2018-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
#ifndef ALEXA_VOICEAGENT_API_INCLUDE
#define ALEXA_VOICEAGENT_API_INCLUDE

#include "ctl-plugin.h"

#ifdef __cplusplus
extern "C" {
#endif

CTLP_ONLOAD(plugin, ret);
CTLP_INIT(plugin, ret);
int setVoiceAgentId(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int setAuthToken(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int subscribe(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int subscribeToCBLEvents(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int wakeword(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int startListening(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int playbackControls(CtlSourceT* source, json_object* argsJ, json_object* queryJ);

// Phone Call Control events coming from VSHL Capabilities.
int onPhoneConnectionStateChanged(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onPhoneCallStateChanged(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onPhoneCallFailed(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onPhoneCallerIdReceived(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onPhoneSendDTMFSucceded(CtlSourceT* source, json_object* argsJ, json_object* queryJ);

// Playback control events coming from VSHL Capabilities.
int onPlaybackButtonPressed(CtlSourceT* source, json_object* argsJ, json_object* queryJ);

// Local MediaSource events coming from VSHL Capabilities.
int onLocalMediaSourceGetStateResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onLocalMediaSourcePlayerEvent(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onLocalMediaSourcePlayerError(CtlSourceT* source, json_object* argsJ, json_object* queryJ);

// Car Control events coming from VSHL Capabilities
int onCarControlClimateIsOnResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlClimateSyncIsOnResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlAirRecirculationIsOnResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlAirConditionerIsOnResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlGetAirConditionerModeResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlHeaterIsOnResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlGetHeaterTemperatureResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlFanIsOnResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlGetFanSpeedResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlVentIsOnResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlGetVentPositionResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlWindowDefrosterIsOnResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlLightIsOnResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);
int onCarControlGetLightColorResponse(CtlSourceT* source, json_object* argsJ, json_object* queryJ);

#ifdef __cplusplus
}
#endif

#endif  // ALEXA_VOICEAGENT_API_INCLUDE

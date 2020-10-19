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

#ifndef AGL_ALEXA_SVC_PHONE_CALL_CAPABILITY_H_
#define AGL_ALEXA_SVC_PHONE_CALL_CAPABILITY_H_

#include <memory>
#include <string>

#include <aasb/interfaces/IAASBController.h>

#include "interfaces/afb/IAFBApi.h"
#include "interfaces/capability/ICapabilityMessageDispatcher.h"
#include "interfaces/utilities/logging/ILogger.h"

namespace agl {
namespace dispatcher {
namespace phonecall {

/**
 * @c PhoneCallDispatcher provides the dispatching functionality to route incoming phone call
 * control messages from vshl-capabilities AGL binding to AASB and similarly to route outgoing
 * messages from AASB to vshl-capabilities AGL binding.
 */
class PhoneCallDispatcher : public agl::capability::interfaces::ICapabilityMessageDispatcher {
public:
    /**
     * Creates a new instance of @c PhoneCallDispatcher.
     *
     * @param logger An instance of logger.
     * @param IAASBController interface to dispatch events to AASB.
     * @param api AFB API instance to communicate with other AGL services (like vshl-capabilities)
     */
    static std::shared_ptr<PhoneCallDispatcher> create(
        std::shared_ptr<agl::common::interfaces::ILogger> logger,
        std::shared_ptr<aasb::bridge::IAASBController> aasbController,
        std::shared_ptr<agl::common::interfaces::IAFBApi> api);

    /// @name ICapabilityMessageDispatcher Functions
    /// @{
    void onReceivedDirective(const std::string& action, const std::string& jsonPayload) override;
    /// @}

    /**
     * Subscribe to receive phone call control events from vshl-capabilities.
     *
     * @return true, if succeeded, false otherwise.
     */
    bool subscribeToPhoneCallControlEvents();

    /**
     * Process the connection state change event from vshl-capabilities.
     *
     * @param payload Data for the event.
     */
    void onConnectionStateChanged(const std::string& payload);

    /**
     * Process the call state change event from vshl-capabilities.
     *
     * @param payload Data for the event.
     */
    void onCallStateChanged(const std::string& payload);

    /**
     * Process the call failed event from vshl-capabilities.
     *
     * @param payload Data for the event.
     */
    void onCallFailed(const std::string& payload);

    /**
     * Process the caller id received event from vshl-capabilities.
     *
     * @param payload Data for the event.
     */
    void onCallerIdReceived(const std::string& payload);

    /**
     * Process the send dtmf succeeded event from vshl-capabilities.
     *
     * @param payload Data for the event.
     */
    void onSendDTMFSucceeded(const std::string& payload);

private:
    /**
     * Constructor for @c PhoneCallDispatcher.
     */
    PhoneCallDispatcher(
        std::shared_ptr<agl::common::interfaces::ILogger> logger,
        std::shared_ptr<aasb::bridge::IAASBController> m_aasbController,
        std::shared_ptr<agl::common::interfaces::IAFBApi> api);

    // Logger.
    std::shared_ptr<agl::common::interfaces::ILogger> m_logger;

    // AASB controller to dispatch events to AASB.
    std::shared_ptr<aasb::bridge::IAASBController> m_aasbController;

    // AFB API object for events pub/sub, and for calling other AGL services.
    std::shared_ptr<agl::common::interfaces::IAFBApi> m_api;
};

}  // namespace phonecall
}  // namespace dispatcher
}  // namespace agl

#endif  // AGL_ALEXA_SVC_PHONE_CALL_CAPABILITY_H_

/*
 * Copyright 2017-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

#include "AACE/Engine/PhoneCallController/PhoneCallControllerEngineService.h"
#include "AACE/Engine/Alexa/AlexaEngineService.h"
#include "AACE/Engine/Core/EngineMacros.h"

namespace aace {
namespace engine {
namespace phoneCallController {

// String to identify log entries originating from this file.
static const std::string TAG("aace.phoneCallController.PhoneCallControllerEngineService");

// register the service
REGISTER_SERVICE(PhoneCallControllerEngineService);

PhoneCallControllerEngineService::PhoneCallControllerEngineService( const aace::engine::core::ServiceDescription& description ) : aace::engine::core::EngineService( description ) {
}

bool PhoneCallControllerEngineService::shutdown() {
    if ( m_phoneCallControllerEngineImpl != nullptr ) {
        m_phoneCallControllerEngineImpl->shutdown();
        m_phoneCallControllerEngineImpl.reset();
    } 
    return true;
}

bool PhoneCallControllerEngineService::registerPlatformInterface( std::shared_ptr<aace::core::PlatformInterface> platformInterface ) {
    try {
        ReturnIf( registerPlatformInterfaceType<aace::phoneCallController::PhoneCallController>( platformInterface ), true );
        return false;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"registerPlatformInterface").d("reason", ex.what()));
        return false;
    }
}

bool PhoneCallControllerEngineService::registerPlatformInterfaceType( std::shared_ptr<aace::phoneCallController::PhoneCallController> phoneCallController )
{
    try
    {
        ThrowIfNotNull( m_phoneCallControllerEngineImpl, "platformInterfaceAlreadyRegistered" );

        auto alexaComponents = getContext()->getServiceInterface<aace::engine::alexa::AlexaComponentInterface>( "aace.alexa" );
        ThrowIfNull( alexaComponents, "invalidAlexaComponentInterface" );

        auto directiveSequencer = alexaComponents->getDirectiveSequencer();
        ThrowIfNull( directiveSequencer, "directiveSequencerInvalid" );

        auto capabilitiesDelegate = alexaComponents->getCapabilitiesDelegate();
        ThrowIfNull( capabilitiesDelegate, "capabilitiesDelegateInvalid" );

        auto exceptionSender = alexaComponents->getExceptionEncounteredSender();
        ThrowIfNull( exceptionSender, "exceptionSenderInvalid" );

        auto messageSender = alexaComponents->getMessageSender();
        ThrowIfNull( messageSender, "messageSenderInvalid" );

        auto contextManager = alexaComponents->getContextManager();
        ThrowIfNull( contextManager, "contextManagerInvalid" );

        auto focusManager = alexaComponents->getAudioFocusManager();
        ThrowIfNull( focusManager, "focusManagerInvalid" );

        m_phoneCallControllerEngineImpl = aace::engine::phoneCallController::PhoneCallControllerEngineImpl::create( phoneCallController, capabilitiesDelegate, contextManager, directiveSequencer, exceptionSender, messageSender, focusManager );
        ThrowIfNull( m_phoneCallControllerEngineImpl, "createPhoneCallControllerEngineImplFailed" );

        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"registerPlatformInterfaceType<PhoneCallController>").d("reason", ex.what()));
        return false;
    }
}

} // aace::engine::phoneCallController
} // aace::engine
} // aace

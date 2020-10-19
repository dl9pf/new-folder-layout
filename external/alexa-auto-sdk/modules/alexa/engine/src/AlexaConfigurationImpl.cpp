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

#include "AACE/Alexa/AlexaConfiguration.h"
#include "AACE/Engine/Utils/JSON/JSON.h"
#include "AACE/Engine/Core/EngineMacros.h"

namespace aace {
namespace alexa {
namespace config {

// String to identify log entries originating from this file.
static const std::string TAG("aace.alexa.config.AlexaConfiguationImpl");

std::shared_ptr<aace::core::config::EngineConfiguration> AlexaConfiguration::createDeviceInfoConfig( const std::string& deviceSerialNumber, const std::string& clientId, const std::string& productId )
{
    rapidjson::Document document( rapidjson::kObjectType );
    rapidjson::Value aaceAlexaElement( rapidjson::kObjectType );
    rapidjson::Value avsDeviceSDKElement( rapidjson::kObjectType );
    rapidjson::Value deviceInfoElement( rapidjson::kObjectType );

    deviceInfoElement.AddMember( "deviceSerialNumber", rapidjson::Value().SetString( deviceSerialNumber.c_str(), deviceSerialNumber.length() ), document.GetAllocator() );
    deviceInfoElement.AddMember( "clientId", rapidjson::Value().SetString( clientId.c_str(), clientId.length() ), document.GetAllocator() );
    deviceInfoElement.AddMember( "productId", rapidjson::Value().SetString( productId.c_str(), productId.length() ), document.GetAllocator() );
    
    avsDeviceSDKElement.AddMember( "deviceInfo", deviceInfoElement, document.GetAllocator() );
    aaceAlexaElement.AddMember( "avsDeviceSDK", avsDeviceSDKElement, document.GetAllocator() );
    document.AddMember( "aace.alexa", aaceAlexaElement, document.GetAllocator() );

    return aace::core::config::StreamConfiguration::create( aace::engine::utils::json::toStream( document ) );
}

std::shared_ptr<aace::core::config::EngineConfiguration> AlexaConfiguration::createAlertsConfig( const std::string& databaseFilePath )
{
    rapidjson::Document document( rapidjson::kObjectType );
    rapidjson::Value aaceAlexaElement( rapidjson::kObjectType );
    rapidjson::Value avsDeviceSDKElement( rapidjson::kObjectType );
    rapidjson::Value alertsCapabilityAgentElement( rapidjson::kObjectType );

    alertsCapabilityAgentElement.AddMember( "databaseFilePath", rapidjson::Value().SetString( databaseFilePath.c_str(), databaseFilePath.length() ), document.GetAllocator() );
    avsDeviceSDKElement.AddMember( "alertsCapabilityAgent", alertsCapabilityAgentElement, document.GetAllocator() );
    aaceAlexaElement.AddMember( "avsDeviceSDK", avsDeviceSDKElement, document.GetAllocator() );
    document.AddMember( "aace.alexa", aaceAlexaElement, document.GetAllocator() );

    return aace::core::config::StreamConfiguration::create( aace::engine::utils::json::toStream( document ) );
}

std::shared_ptr<aace::core::config::EngineConfiguration> AlexaConfiguration::createNotificationsConfig( const std::string& databaseFilePath )
{
    rapidjson::Document document( rapidjson::kObjectType );
    rapidjson::Value aaceAlexaElement( rapidjson::kObjectType );
    rapidjson::Value avsDeviceSDKElement( rapidjson::kObjectType );
    rapidjson::Value notificationsElement( rapidjson::kObjectType );

    notificationsElement.AddMember( "databaseFilePath", rapidjson::Value().SetString( databaseFilePath.c_str(), databaseFilePath.length() ), document.GetAllocator() );
    avsDeviceSDKElement.AddMember( "notifications", notificationsElement, document.GetAllocator() );
    aaceAlexaElement.AddMember( "avsDeviceSDK", avsDeviceSDKElement, document.GetAllocator() );
    document.AddMember( "aace.alexa", aaceAlexaElement, document.GetAllocator() );

    return aace::core::config::StreamConfiguration::create( aace::engine::utils::json::toStream( document ) );
}

std::shared_ptr<aace::core::config::EngineConfiguration> AlexaConfiguration::createCertifiedSenderConfig( const std::string& databaseFilePath )
{
    rapidjson::Document document( rapidjson::kObjectType );
    rapidjson::Value aaceAlexaElement( rapidjson::kObjectType );
    rapidjson::Value avsDeviceSDKElement( rapidjson::kObjectType );
    rapidjson::Value certifiedSenderElement( rapidjson::kObjectType );

    certifiedSenderElement.AddMember( "databaseFilePath", rapidjson::Value().SetString( databaseFilePath.c_str(), databaseFilePath.length() ), document.GetAllocator() );
    
    avsDeviceSDKElement.AddMember( "certifiedSender", certifiedSenderElement, document.GetAllocator() );

    aaceAlexaElement.AddMember( "avsDeviceSDK", avsDeviceSDKElement, document.GetAllocator() );
    document.AddMember( "aace.alexa", aaceAlexaElement, document.GetAllocator() );

    return aace::core::config::StreamConfiguration::create( aace::engine::utils::json::toStream( document ) );
}

std::shared_ptr<aace::core::config::EngineConfiguration> AlexaConfiguration::createCurlConfig( const std::string &certsPath, const std::string &iface )
{
    rapidjson::Document document( rapidjson::kObjectType );
    rapidjson::Value aaceAlexaElement( rapidjson::kObjectType );
    rapidjson::Value avsDeviceSDKElement( rapidjson::kObjectType );
    rapidjson::Value libcurlUtilsElement( rapidjson::kObjectType );

    libcurlUtilsElement.AddMember( "CURLOPT_CAPATH", rapidjson::Value().SetString( certsPath.c_str(), certsPath.length() ), document.GetAllocator());
    
    if( iface.length() > 0 ) {
        libcurlUtilsElement.AddMember("CURLOPT_INTERFACE", rapidjson::Value().SetString(iface.c_str(), iface.length()), document.GetAllocator());
    }
    
    avsDeviceSDKElement.AddMember( "libcurlUtils", libcurlUtilsElement, document.GetAllocator() );
    aaceAlexaElement.AddMember( "avsDeviceSDK", avsDeviceSDKElement, document.GetAllocator() );
    document.AddMember( "aace.alexa", aaceAlexaElement, document.GetAllocator() );
    
    return aace::core::config::StreamConfiguration::create( aace::engine::utils::json::toStream( document ) );
}

std::shared_ptr<aace::core::config::EngineConfiguration> AlexaConfiguration::createSettingsConfig( const std::string& databaseFilePath, const std::string& locale )
{
    rapidjson::Document document( rapidjson::kObjectType );
    rapidjson::Value aaceAlexaElement( rapidjson::kObjectType );
    rapidjson::Value avsDeviceSDKElement( rapidjson::kObjectType );
    rapidjson::Value settingsElement( rapidjson::kObjectType );

    settingsElement.AddMember( "databaseFilePath", rapidjson::Value().SetString( databaseFilePath.c_str(), databaseFilePath.length() ), document.GetAllocator() );
    
    rapidjson::Value defaultAVSClientSettingsElement( rapidjson::kObjectType );

    defaultAVSClientSettingsElement.AddMember( "locale", rapidjson::Value().SetString( locale.c_str(), locale.length() ), document.GetAllocator() );
    
    settingsElement.AddMember( "defaultAVSClientSettings", defaultAVSClientSettingsElement, document.GetAllocator() );
    
    avsDeviceSDKElement.AddMember( "settings", settingsElement, document.GetAllocator() );
    
    aaceAlexaElement.AddMember( "avsDeviceSDK", avsDeviceSDKElement, document.GetAllocator() );
    document.AddMember( "aace.alexa", aaceAlexaElement, document.GetAllocator() );

    return aace::core::config::StreamConfiguration::create( aace::engine::utils::json::toStream( document ) );
}

std::shared_ptr<aace::core::config::EngineConfiguration> AlexaConfiguration::createSystemConfig( uint32_t firmwareVersion )
{
    rapidjson::Document document( rapidjson::kObjectType );
    rapidjson::Value aaceAlexaElement( rapidjson::kObjectType );
    rapidjson::Value systemElement( rapidjson::kObjectType );

    systemElement.AddMember( "firmwareVersion", firmwareVersion, document.GetAllocator() );

    aaceAlexaElement.AddMember( "system", systemElement, document.GetAllocator() );

    document.AddMember( "aace.alexa", aaceAlexaElement, document.GetAllocator() );
    
    return aace::core::config::StreamConfiguration::create( aace::engine::utils::json::toStream( document ) );
}

std::shared_ptr<aace::core::config::EngineConfiguration> AlexaConfiguration::createMiscStorageConfig( const std::string& databaseFilePath )
{
    rapidjson::Document document( rapidjson::kObjectType );
    rapidjson::Value aaceAlexaElement( rapidjson::kObjectType );
    rapidjson::Value avsDeviceSDKElement( rapidjson::kObjectType );

    rapidjson::Value certifiedSenderElement( rapidjson::kObjectType );

    certifiedSenderElement.AddMember( "databaseFilePath", rapidjson::Value().SetString( databaseFilePath.c_str(), databaseFilePath.length() ), document.GetAllocator() );
    
    avsDeviceSDKElement.AddMember( "miscDatabase", certifiedSenderElement, document.GetAllocator() );
    
    aaceAlexaElement.AddMember( "avsDeviceSDK", avsDeviceSDKElement, document.GetAllocator() );
    document.AddMember( "aace.alexa", aaceAlexaElement, document.GetAllocator() );

    return aace::core::config::StreamConfiguration::create( aace::engine::utils::json::toStream( document ) );
}

std::shared_ptr<aace::core::config::EngineConfiguration> AlexaConfiguration::createSpeechRecognizerConfig( const std::string& encoderName )
{
    rapidjson::Document document( rapidjson::kObjectType );
    rapidjson::Value aaceAlexaElement( rapidjson::kObjectType );
    rapidjson::Value speechRecognizerElement( rapidjson::kObjectType );
    rapidjson::Value encoderElement( rapidjson::kObjectType );

    encoderElement.AddMember( "name", rapidjson::Value().SetString( encoderName.c_str(), encoderName.length() ), document.GetAllocator() );

    speechRecognizerElement.AddMember( "encoder", encoderElement, document.GetAllocator() );

    aaceAlexaElement.AddMember( "speechRecognizer", speechRecognizerElement, document.GetAllocator() );

    document.AddMember( "aace.alexa", aaceAlexaElement, document.GetAllocator() );

    return aace::core::config::StreamConfiguration::create( aace::engine::utils::json::toStream( document ) );
}

std::shared_ptr<aace::core::config::EngineConfiguration> AlexaConfiguration::createTemplateRuntimeTimeoutConfig( const std::vector<TemplateRuntimeTimeout>& timeoutList )
{
    rapidjson::Document document( rapidjson::kObjectType );
    rapidjson::Value aaceAlexaElement( rapidjson::kObjectType );
    rapidjson::Value avsDeviceSDKElement( rapidjson::kObjectType );
    rapidjson::Value templateRuntimeCapabilityAgentElement( rapidjson::kObjectType );

    using TimeoutType = aace::alexa::config::AlexaConfiguration::TemplateRuntimeTimeoutType;

    for( auto next : timeoutList )
    {
        std::string name;
        
        switch( next.first )
        {
            case TimeoutType::DISPLAY_CARD_TTS_FINISHED_TIMEOUT:
                name = "displayCardTTSFinishedTimeout";
                break;
            
            case TimeoutType::DISPLAY_CARD_AUDIO_PLAYBACK_FINISHED_TIMEOUT:
                name = "displayCardAudioPlaybackFinishedTimeout";
                break;
            
            case TimeoutType::DISPLAY_CARD_AUDIO_PLAYBACK_STOPPED_PAUSED_TIMEOUT:
                name = "displayCardAudioPlaybackStoppedPausedTimeout";
                break;
        }
        
        templateRuntimeCapabilityAgentElement.AddMember(rapidjson::Value().SetString(name.c_str(), name.length(), document.GetAllocator()), rapidjson::Value().SetInt(next.second.count()), document.GetAllocator());
    }

    avsDeviceSDKElement.AddMember("templateRuntimeCapabilityAgent", templateRuntimeCapabilityAgentElement, document.GetAllocator());

    aaceAlexaElement.AddMember( "avsDeviceSDK", avsDeviceSDKElement, document.GetAllocator() );
    document.AddMember( "aace.alexa", aaceAlexaElement, document.GetAllocator() );

    return aace::core::config::StreamConfiguration::create( aace::engine::utils::json::toStream( document ) );
}

std::shared_ptr<aace::core::config::EngineConfiguration> AlexaConfiguration::createExternalMediaPlayerConfig( const std::string& agent )
{
    rapidjson::Document document( rapidjson::kObjectType );
    rapidjson::Value aaceAlexaElement( rapidjson::kObjectType );
    rapidjson::Value externalMediaPlayerElement( rapidjson::kObjectType );

    externalMediaPlayerElement.AddMember( "agent", rapidjson::Value().SetString( agent.c_str(), agent.length() ), document.GetAllocator() );
    aaceAlexaElement.AddMember( "externalMediaPlayer", externalMediaPlayerElement, document.GetAllocator() );
    
    document.AddMember( "aace.alexa", aaceAlexaElement, document.GetAllocator() );

    return aace::core::config::StreamConfiguration::create( aace::engine::utils::json::toStream( document ) );
}

std::shared_ptr<aace::core::config::EngineConfiguration> AlexaConfiguration::createEqualizerControllerConfig(
    const std::vector<EqualizerBand>& supportedBands,
    int minLevel,
    int maxLevel,
    const std::vector<EqualizerBandLevel>& defaultBandLevels ) {

    rapidjson::Document document( rapidjson::kObjectType );
    rapidjson::Value aaceAlexaElement( rapidjson::kObjectType );
    rapidjson::Value avsDeviceSDKElement( rapidjson::kObjectType );
    rapidjson::Value equalizerElement(rapidjson::kObjectType);

    // enabled
    equalizerElement.AddMember( "enabled", rapidjson::Value().SetBool(true), document.GetAllocator() );

    // minLevel
    equalizerElement.AddMember( "minLevel", rapidjson::Value().SetInt(minLevel), document.GetAllocator() );

    // maxLevel
    equalizerElement.AddMember( "maxLevel", rapidjson::Value().SetInt(maxLevel), document.GetAllocator() );

    // bands
    if( supportedBands.size() != 0 ) {
        rapidjson::Value bandsElement(rapidjson::kObjectType);
        for( const auto& band : supportedBands ) {
            const std::string& name = equalizerBandToString( band );
            bandsElement.AddMember( 
                rapidjson::Value().SetString(name.c_str(), name.length(), document.GetAllocator()),
                rapidjson::Value().SetBool(true), document.GetAllocator() );
        }
        equalizerElement.AddMember( "bands", bandsElement, document.GetAllocator() );
    }

    // defaultState
    if( defaultBandLevels.size() != 0 ) {
        rapidjson::Value defaultStateElement(rapidjson::kObjectType);
        
        // defaultState.bands
        rapidjson::Value defaultStateBandsElement(rapidjson::kObjectType);
        for( const auto& band : defaultBandLevels ) {
            const std::string& bandName = equalizerBandToString( band.first );
            defaultStateBandsElement.AddMember( 
                rapidjson::Value().SetString(bandName.c_str(), bandName.length(), document.GetAllocator()), 
                rapidjson::Value().SetInt(band.second), 
                document.GetAllocator() );
        }
        defaultStateElement.AddMember( "bands", defaultStateBandsElement, document.GetAllocator() );
        equalizerElement.AddMember( "defaultState", defaultStateElement, document.GetAllocator() );
    }

    avsDeviceSDKElement.AddMember( "equalizer", equalizerElement, document.GetAllocator() );

    aaceAlexaElement.AddMember( "avsDeviceSDK", avsDeviceSDKElement, document.GetAllocator() );
    document.AddMember( "aace.alexa", aaceAlexaElement, document.GetAllocator() );

    return aace::core::config::StreamConfiguration::create( aace::engine::utils::json::toStream( document ) );
}

} // aace::alexa::config
} // aace::alexa
} // aace

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

#include <AVSCommon/AVS/EventBuilder.h>

#include "AACE/Engine/Alexa/ExternalMediaAdapterHandler.h"
#include "AACE/Engine/Core/EngineMacros.h"

#include <rapidjson/document.h>
#include <rapidjson/prettywriter.h>
#include <rapidjson/stringbuffer.h>
#include <rapidjson/error/en.h>

namespace aace {
namespace engine {
namespace alexa {

static const uint8_t DEFAULT_SPEAKER_VOLUME = 50;

// String to identify log entries originating from this file.
static const std::string TAG("aace.alexa.ExternalMediaAdapterHandler");

// external media player agent constant
static const std::string EXTERNAL_MEDIA_PLAYER_AGENT = "alexaAutoSDK";

ExternalMediaAdapterHandler::ExternalMediaAdapterHandler( std::shared_ptr<DiscoveredPlayerSenderInterface> discoveredPlayerSender, std::shared_ptr<FocusHandlerInterface> focusHandler ) :
    alexaClientSDK::avsCommon::utils::RequiresShutdown(TAG),
    m_discoveredPlayerSender( discoveredPlayerSender ), 
    m_focusHandler( focusHandler ),
    m_muted( false ),
    m_volume( DEFAULT_SPEAKER_VOLUME ) {
}

bool ExternalMediaAdapterHandler::initializeAdapterHandler( std::shared_ptr<alexaClientSDK::avsCommon::sdkInterfaces::SpeakerManagerInterface> speakerManager )
{
    try
    {
        ThrowIfNull( speakerManager, "invalidSpeakerManager" );

        // add the speaker impl to the speaker manager
        speakerManager->addSpeaker( shared_from_this() );

        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"initializeAdapterHandler").d("reason", ex.what()));
        return false;
    }
}

bool ExternalMediaAdapterHandler::validatePlayer( const std::string& localPlayerId, bool checkAuthorized ) {
    auto it = m_playerInfoMap.find( localPlayerId );
    return it != m_playerInfoMap.end() && (it->second.authorized || checkAuthorized == false);
}

bool ExternalMediaAdapterHandler::setFocus( const std::string& localPlayerId )
{
    try
    {
        ThrowIfNot( validatePlayer( localPlayerId ), "invalidPlayerInfo" );
        auto playerInfo = m_playerInfoMap[localPlayerId];
        
        auto m_focusHandler_lock = m_focusHandler.lock();
        ThrowIfNull( m_focusHandler_lock, "invalidFocusHandler" );
        
        m_focusHandler_lock->setFocus( playerInfo.playerId );
        
        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"setFocus").d("reason", ex.what()));
        return false;
    }
}

std::vector<PlayerInfo> ExternalMediaAdapterHandler::authorizeDiscoveredPlayers( const std::vector<PlayerInfo>& authorizedPlayerList )
{
    try
    {
        std::vector<aace::alexa::ExternalMediaAdapter::AuthorizedPlayerInfo> authorizedPlayerInfoList;
        std::vector<PlayerInfo> supportedPlayerList;

        for( const auto& next : authorizedPlayerList )
        {
            if( validatePlayer( next.localPlayerId, false ) )
            {
                aace::alexa::ExternalMediaAdapter::AuthorizedPlayerInfo info;
                
                info.localPlayerId = next.localPlayerId;
                info.authorized = next.authorized;
            
                authorizedPlayerInfoList.push_back( info );
                supportedPlayerList.push_back( next );
                
                // copy the player info into the player info map
                m_playerInfoMap[next.localPlayerId] = next;
                
                // add an entry to the alexa to local player id map
                m_alexaToLocalPlayerIdMap[next.playerId] = next.localPlayerId;
            }
        }

        if( authorizedPlayerInfoList.empty() == false ) {
            ThrowIfNot( handleAuthorization( authorizedPlayerInfoList ), "handleAuthorizeFailed" );
        }

        return supportedPlayerList;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"authorizeDiscoveredPlayers").d("reason", ex.what()));
        return  std::vector<PlayerInfo>();
    }
}

bool ExternalMediaAdapterHandler::login( const std::string& playerId, const std::string& accessToken, const std::string& userName, bool forceLogin, std::chrono::milliseconds tokenRefreshInterval )
{
    try
    {
        auto it = m_alexaToLocalPlayerIdMap.find( playerId );
        ThrowIf( it == m_alexaToLocalPlayerIdMap.end(), "invalidPlayerId" );
    
        // call the platform media adapter
        ThrowIfNot( handleLogin( it->second, accessToken, userName, forceLogin, tokenRefreshInterval ), "handleLoginFailed" );
    
        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"login").d("reason", ex.what()));
        return false;
    }
}

bool ExternalMediaAdapterHandler::logout( const std::string& playerId )
{
    try
    {
        auto it = m_alexaToLocalPlayerIdMap.find( playerId );
        ThrowIf( it == m_alexaToLocalPlayerIdMap.end(), "invalidPlayerId" );
    
        // call the platform media adapter
        ThrowIfNot( handleLogout( it->second ), "handleLogoutFailed" );
    
        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"logout").d("reason", ex.what()));
        return false;
    }
}

bool ExternalMediaAdapterHandler::play( const std::string& playerId, const std::string& playContextToken, int64_t index, std::chrono::milliseconds offset, const std::string& skillToken, const std::string& playbackSessionId, bool preload, aace::alexa::ExternalMediaAdapter::Navigation navigation )
{
    try
    {
        auto it = m_alexaToLocalPlayerIdMap.find( playerId );
        ThrowIf( it == m_alexaToLocalPlayerIdMap.end(), "invalidPlayerId" );
    
        // get the local player id
        auto localPlayerId = it->second;
    
        // update the player info
        ThrowIfNot( validatePlayer( localPlayerId ), "invalidPlayerInfo" );
        auto playerInfo = m_playerInfoMap[localPlayerId];
    
        playerInfo.skillToken = skillToken;
        playerInfo.playbackSessionId = playbackSessionId;
    
        // call the platform media adapter
        ThrowIfNot( handlePlay( localPlayerId, playContextToken, index, offset, preload, navigation ), "handlePlayFailed" );
    
        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"play").d("reason", ex.what()));
        return false;
    }
}

bool ExternalMediaAdapterHandler::playControl( const std::string& playerId, alexaClientSDK::avsCommon::sdkInterfaces::externalMediaPlayer::RequestType requestType )
{
    try
    {
        auto it = m_alexaToLocalPlayerIdMap.find( playerId );
        ThrowIf( it == m_alexaToLocalPlayerIdMap.end(), "invalidPlayerId" );
    
        // convert RequestType to PlayControlType
        using RequestType = alexaClientSDK::avsCommon::sdkInterfaces::externalMediaPlayer::RequestType;
        using PlayControlType = aace::alexa::ExternalMediaAdapter::PlayControlType;
        
        PlayControlType controlType;
        
        switch( requestType )
        {
            case RequestType::PAUSE: controlType = PlayControlType::PAUSE; break;
            case RequestType::RESUME: controlType = PlayControlType::RESUME; break;
            case RequestType::NEXT: controlType = PlayControlType::NEXT; break;
            case RequestType::PREVIOUS: controlType = PlayControlType::PREVIOUS; break;
            case RequestType::START_OVER: controlType = PlayControlType::START_OVER; break;
            case RequestType::FAST_FORWARD: controlType = PlayControlType::FAST_FORWARD; break;
            case RequestType::REWIND: controlType = PlayControlType::REWIND; break;
            case RequestType::ENABLE_REPEAT_ONE: controlType = PlayControlType::ENABLE_REPEAT_ONE; break;
            case RequestType::ENABLE_REPEAT: controlType = PlayControlType::ENABLE_REPEAT; break;
            case RequestType::DISABLE_REPEAT: controlType = PlayControlType::DISABLE_REPEAT; break;
            case RequestType::ENABLE_SHUFFLE: controlType = PlayControlType::ENABLE_SHUFFLE; break;
            case RequestType::DISABLE_SHUFFLE: controlType = PlayControlType::DISABLE_SHUFFLE; break;
            case RequestType::FAVORITE: controlType = PlayControlType::FAVORITE; break;
            case RequestType::UNFAVORITE: controlType = PlayControlType::UNFAVORITE; break;
            default: Throw( "unsupportedRequestType" );
        }
    
        // call the platform media adapter
        ThrowIfNot( handlePlayControl( it->second, controlType ), "handlePlayControlFailed" );
    
        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"playControl").d("reason", ex.what()));
        return false;
    }
}

bool ExternalMediaAdapterHandler::seek( const std::string& playerId, std::chrono::milliseconds offset )
{
    try
    {
        auto it = m_alexaToLocalPlayerIdMap.find( playerId );
        ThrowIf( it == m_alexaToLocalPlayerIdMap.end(), "invalidPlayerId" );
    
        // call the platform media adapter
        ThrowIfNot( handleSeek( it->second, offset ), "handleSeekFailed" );
    
        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"seek").d("reason", ex.what()));
        return false;
    }
}

bool ExternalMediaAdapterHandler::adjustSeek( const std::string& playerId, std::chrono::milliseconds deltaOffset )
{
    try
    {
        auto it = m_alexaToLocalPlayerIdMap.find( playerId );
        ThrowIf( it == m_alexaToLocalPlayerIdMap.end(), "invalidPlayerId" );
    
        // call the platform media adapter
        ThrowIfNot( handleAdjustSeek( it->second, deltaOffset ), "handleAdjustSeekFailed" );
    
        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"adjustSeek").d("reason", ex.what()));
        return false;
    }
}

std::vector<alexaClientSDK::avsCommon::sdkInterfaces::externalMediaPlayer::AdapterState> ExternalMediaAdapterHandler::getAdapterStates()
{
    try
    {
        std::vector<alexaClientSDK::avsCommon::sdkInterfaces::externalMediaPlayer::AdapterState> adapterStateList;

        for( const auto& next : m_playerInfoMap )
        {
            auto playerInfo = next.second;
            
            if( playerInfo.authorized )
            {
                alexaClientSDK::avsCommon::sdkInterfaces::externalMediaPlayer::AdapterState state;
                
                // default session state
                state.sessionState.playerId = playerInfo.playerId;
                state.sessionState.skillToken = playerInfo.skillToken;
                state.sessionState.playbackSessionId = playerInfo.playbackSessionId;
                state.sessionState.spiVersion = playerInfo.spiVersion;

                // default playback state
                state.playbackState.playerId = playerInfo.playerId;

                // get the player state from the adapter implementation
                ThrowIfNot( handleGetAdapterState( playerInfo.localPlayerId, state ), "handleGetAdapterStateFailed" );

                adapterStateList.push_back( state );
            }
        }
        
        return adapterStateList;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"getAdapterStates").d("reason", ex.what()));
        return std::vector<alexaClientSDK::avsCommon::sdkInterfaces::externalMediaPlayer::AdapterState>();
    }
}

std::string ExternalMediaAdapterHandler::createExternalMediaPlayerEvent( const std::string& localPlayerId, const std::string& event, bool includePlaybackSessionId, std::function<void(rapidjson::Value::Object&,rapidjson::Value::AllocatorType&)> createPayload )
{
    try
    {
        ThrowIfNot( validatePlayer( localPlayerId ), "invalidPlayerInfo" );
        auto playerInfo = m_playerInfoMap[localPlayerId];

        // create the event payload
        rapidjson::Document document( rapidjson::kObjectType );
        
        // create payload data
        auto payload = document.GetObject();

        // call the lamda createPayload() function
        createPayload( payload, document.GetAllocator() );

        payload.AddMember( "playerId", rapidjson::Value().SetString( playerInfo.playerId.c_str(), playerInfo.playerId.length() ), document.GetAllocator() );
        payload.AddMember( "skillToken", rapidjson::Value().SetString( playerInfo.skillToken.c_str(), playerInfo.skillToken.length() ), document.GetAllocator() );
        
        if( includePlaybackSessionId ) {
            payload.AddMember( "playbackSessionId", rapidjson::Value().SetString( playerInfo.playbackSessionId.c_str(), playerInfo.playbackSessionId.length() ), document.GetAllocator() );
        }
        
        // create payload string
        rapidjson::StringBuffer buffer;
        rapidjson::PrettyWriter<rapidjson::StringBuffer> writer( buffer );

        document.Accept( writer );
    
        return alexaClientSDK::avsCommon::avs::buildJsonEventString( "ExternalMediaPlayer", event, "", buffer.GetString() ).second;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"createExternalMediaPlayerEvent").d("reason", ex.what()));
        return "";
    }
}


//
// alexaClientSDK::avsCommon::sdkInterfaces::SpeakerInterface
//

bool ExternalMediaAdapterHandler::setVolume( int8_t volume )
{
    try
    {
        ThrowIfNot( handleSetVolume( volume ), "handleSetVolumeFailed" );
        
        m_volume = volume;
    
        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG).d("reason", ex.what()));
        return false;
    }
}

bool ExternalMediaAdapterHandler::adjustVolume( int8_t delta )
{
    try
    {
        ThrowIfNot( setVolume( m_volume + delta ), "setVolumeFailed" );
        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG).d("reason", ex.what()));
        return false;
    }
}

bool ExternalMediaAdapterHandler::setMute( bool mute )
{
    try
    {
        m_muted = mute;

        ThrowIfNot( handleSetMute( mute ), "handleSetMuteFailed" );
        
        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG).d("reason", ex.what()));
        return false;
    }
}

bool ExternalMediaAdapterHandler::getSpeakerSettings( alexaClientSDK::avsCommon::sdkInterfaces::SpeakerInterface::SpeakerSettings* settings )
{
    try
    {
        settings->volume = m_volume;
        settings->mute = m_muted;
        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG).d("reason", ex.what()));
        return false;
    }
}

alexaClientSDK::avsCommon::sdkInterfaces::SpeakerInterface::Type ExternalMediaAdapterHandler::getSpeakerType() {
    return alexaClientSDK::avsCommon::sdkInterfaces::SpeakerInterface::Type::AVS_SPEAKER_VOLUME;
}

void ExternalMediaAdapterHandler::reportDiscoveredPlayers( const std::vector<aace::alexa::ExternalMediaAdapter::DiscoveredPlayerInfo>& discoveredPlayers )
{
    try
    {
        auto m_discoveredPlayerSender_lock = m_discoveredPlayerSender.lock();
        ThrowIfNull( m_discoveredPlayerSender_lock, "invalidDiscoveredPlayerSender" );
    
        // add the player info to the registered player map
        for( const auto& next : discoveredPlayers ) {
            m_playerInfoMap[next.localPlayerId] = PlayerInfo( next.localPlayerId, next.spiVersion );
        }

        // used the discovered player sender to report the players
        m_discoveredPlayerSender_lock->reportDiscoveredPlayers( discoveredPlayers );
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"reportDiscoveredPlayers").d("reason", ex.what()));
    }
}

bool ExternalMediaAdapterHandler::removeDiscoveredPlayer( const std::string& localPlayerId )
{
    try
    {
        auto it = m_playerInfoMap.find( localPlayerId );
        ThrowIf( it == m_playerInfoMap.end(), "invalidLocalPlayerId" );
        
        // remove the player form the alexa to local player id map
        m_alexaToLocalPlayerIdMap.erase( it->second.playerId );
        
        // remove the player info map entry
        m_playerInfoMap.erase( it );
        
        auto m_discoveredPlayerSender_lock = m_discoveredPlayerSender.lock();
        ThrowIfNull( m_discoveredPlayerSender_lock, "invalidDiscoveredPlayerSender" );

        // notify the discovered player sender that the player has been removed
        m_discoveredPlayerSender_lock->removeDiscoveredPlayer( localPlayerId );
        
        return true;
    }
    catch( std::exception& ex ) {
        AACE_ERROR(LX(TAG,"removeDiscoveredPlayer").d("reason", ex.what()).d("localPlayerId",localPlayerId));
        return false;
    }
}

//
// PlayerInfo
//

PlayerInfo::PlayerInfo( const std::string& localId, const std::string& spi, bool authorized ) : localPlayerId( localId ), spiVersion( spi ) {
}


} // aace::engine::alexa
} // aace::engine
} // aace

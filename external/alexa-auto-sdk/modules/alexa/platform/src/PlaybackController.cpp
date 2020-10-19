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

#include "AACE/Alexa/PlaybackController.h"

namespace aace {
namespace alexa {

PlaybackController::~PlaybackController() = default; // key function

void PlaybackController::buttonPressed(PlaybackButton button) {
    if( auto m_playbackControllerEngineInterface_lock = m_playbackControllerEngineInterface.lock() ) {
        m_playbackControllerEngineInterface_lock->onButtonPressed(button);
    }
}

void PlaybackController::togglePressed(PlaybackToggle toggle, bool action) {
    if( auto m_playbackControllerEngineInterface_lock = m_playbackControllerEngineInterface.lock() ) {
        m_playbackControllerEngineInterface_lock->onTogglePressed(toggle, action);
    }
}

void PlaybackController::setEngineInterface( std::shared_ptr<aace::alexa::PlaybackControllerEngineInterface> playbackControllerEngineInterface ) {
    m_playbackControllerEngineInterface = playbackControllerEngineInterface;
}

} // aace::alexa
} // aac

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

#ifndef SAMPLEAPP_ALEXA_PLAYBACKCONTROLLERHANDLER_H
#define SAMPLEAPP_ALEXA_PLAYBACKCONTROLLERHANDLER_H

#include "SampleApp/Activity.h"
#include "SampleApp/Logger/LoggerHandler.h"

#include <AACE/Alexa/PlaybackController.h>

namespace sampleApp {
namespace alexa {

////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  PlaybackControllerHandler
//
////////////////////////////////////////////////////////////////////////////////////////////////////

class PlaybackControllerHandler : public aace::alexa::PlaybackController /* isa PlatformInterface */ {
  private:
    std::weak_ptr<Activity> m_activity{};
    std::weak_ptr<logger::LoggerHandler> m_loggerHandler{};

  protected:
    PlaybackControllerHandler(std::weak_ptr<Activity> activity, std::weak_ptr<logger::LoggerHandler> loggerHandler);

  public:
    template <typename... Args> static auto create(Args &&... args) -> std::shared_ptr<PlaybackControllerHandler> {
        return std::shared_ptr<PlaybackControllerHandler>(new PlaybackControllerHandler(args...));
    }
    auto getActivity() -> std::weak_ptr<Activity>;
    auto getLoggerHandler() -> std::weak_ptr<logger::LoggerHandler>;

  private:
    std::weak_ptr<View> m_console{};

    auto log(logger::LoggerHandler::Level level, const std::string &message) -> void;
    auto setupUI() -> void;
};

} // namespace alexa
} // namespace sampleApp

#endif // SAMPLEAPP_ALEXA_PLAYBACKCONTROLLERHANDLER_H

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

#ifndef AACE_ENGINE_TEST_AUDIO_MOCK_AUDIO_MANAGER_INTERFACE_H
#define AACE_ENGINE_TEST_AUDIO_MOCK_AUDIO_MANAGER_INTERFACE_H

#include "AACE/Engine/Audio/AudioManagerInterface.h"
#include <gtest/gtest.h>

namespace aace {
namespace test {
namespace audio {

class MockAudioManagerInterface : public aace::engine::audio::AudioManagerInterface {
public:
    MOCK_METHOD2(openAudioInputChannel,
        std::shared_ptr<aace::engine::audio::AudioInputChannelInterface>(const std::string& name,AudioInputType audioInputType)
    );
    MOCK_METHOD2(openAudioOutputChannel,
        std::shared_ptr<aace::engine::audio::AudioOutputChannelInterface>(const std::string& name,AudioOutputType audioOutputType)
    );
};

} // aace::test::audio
} // aace::test
} // aace

#endif // AACE_ENGINE_TEST_AUDIO_MOCK_AUDIO_MANAGER_INTERFACE_H

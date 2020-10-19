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

#ifndef AACE_ENGINE_LOGGER_THREADMONIKER_H
#define AACE_ENGINE_LOGGER_THREADMONIKER_H

#include <string>

namespace aace {
namespace engine {
namespace logger {

class ThreadMoniker {
public:
    ThreadMoniker();

    static inline const std::string& getThisThreadMoniker();

private:
    std::string m_moniker;

    static thread_local ThreadMoniker m_threadMoniker;
};

const std::string& ThreadMoniker::getThisThreadMoniker() {
    return m_threadMoniker.m_moniker;
}

}  // logger
}  // engine
}  // aace

#endif // AACE_ENGINE_LOGGER_THREADMONIKER_H

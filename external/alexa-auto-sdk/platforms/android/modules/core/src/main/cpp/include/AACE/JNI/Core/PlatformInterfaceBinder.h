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

#ifndef AACE_JNI_CORE_PLATFORM_INTERFACE_BINDER_H
#define AACE_JNI_CORE_PLATFORM_INTERFACE_BINDER_H

#include <AACE/Core/PlatformInterface.h>
#include "NativeLib.h"

namespace aace {
namespace jni {
namespace core {

    class PlatformInterfaceBinder {
    protected:
        PlatformInterfaceBinder() = default;

    public:
        virtual ~PlatformInterfaceBinder();

        virtual std::shared_ptr<aace::core::PlatformInterface> getPlatformInterface() = 0;
    };

}  // aace::jni::core
}  // aace::jni
}  // aace

#endif // AACE_JNI_CORE_PLATFORM_INTERFACE_BINDER_H

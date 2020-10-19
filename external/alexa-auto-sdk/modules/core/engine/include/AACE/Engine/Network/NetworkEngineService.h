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

#ifndef AACE_ENGINE_NETWORK_NETWORK_ENGINE_SERVICE_H
#define AACE_ENGINE_NETWORK_NETWORK_ENGINE_SERVICE_H

#include "AACE/Engine/Core/EngineService.h"
#include "AACE/Network/NetworkInfoProvider.h"

#include "NetworkInfoProviderEngineImpl.h"

namespace aace {
namespace engine {
namespace network {

class NetworkEngineService : public aace::engine::core::EngineService {
public:
    DESCRIBE("aace.network",VERSION("1.0"))

private:
    NetworkEngineService( const aace::engine::core::ServiceDescription& description );
    
public:
    virtual ~NetworkEngineService() = default;
    
protected:
    bool registerPlatformInterface( std::shared_ptr<aace::core::PlatformInterface> platformInterface ) override;
    bool setProperty( const std::string& key, const std::string& value ) override;
    std::string getProperty( const std::string& key ) override;

private:

    // platform interface registration
    template <class T>
    bool registerPlatformInterfaceType( std::shared_ptr<aace::core::PlatformInterface> platformInterface ) {
        std::shared_ptr<T> typedPlatformInterface = std::dynamic_pointer_cast<T>( platformInterface );
        return typedPlatformInterface != nullptr ? registerPlatformInterfaceType( typedPlatformInterface ) : false;
    }
    
    bool registerPlatformInterfaceType( std::shared_ptr<aace::network::NetworkInfoProvider> networkInfoProvider );

private:
    std::shared_ptr<NetworkInfoProviderEngineImpl> m_networkInfoProviderEngineImpl;
    std::shared_ptr<aace::network::NetworkInfoProvider> m_networkInfoProvider;
};

} // aace::engine::network
} // aace::engine
} // aace

#endif // AACE_ENGINE_NETWORK_NETWORK_ENGINE_SERVICE_H

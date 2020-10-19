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
#ifndef AASB_LOCATION_LOCATIONPROVIDER_HANDLER_H
#define AASB_LOCATION_LOCATIONPROVIDER_HANDLER_H

#include <condition_variable>
#include <memory>
#include <mutex>

#include <AACE/Location/LocationProvider.h>

#include <aasb/interfaces/IConfigurationProvider.h>
#include "ResponseDispatcher.h"
#include "LoggerHandler.h"

namespace aasb {
namespace location {

/**
 * Platform implementation for @c aace::location::LocationProvider.
 *
 * All location requests will be routed to AASB clients which will then have to provide
 * a response for such requests.
 */
class LocationProviderHandler : public aace::location::LocationProvider {
public:
    /**
     * Creates an instance of @c LocationProviderHandler.
     *
     * @param logger An instance of logger.
     * @param config Instance of config object to obtain country and location.
     * @param responseDispatcher An object through which the directives for phone call control
     *      received from alexa cloud will be dispatched to AASB clients.
     */
    static std::shared_ptr<LocationProviderHandler> create(
        std::shared_ptr<aasb::core::logger::LoggerHandler> logger,
        std::shared_ptr<aasb::bridge::IConfigurationProvider> config,
        std::weak_ptr<aasb::bridge::ResponseDispatcher> responseDispatcher);

    /// @name aace::location::LocationProvider
    /// @{
    aace::location::Location getLocation() override;
    std::string getCountry() override;
    /// @}

    /**
     * Process incoming events from AASB client meant for topic @c TOPIC_LOCATIONPROVIDER
     *
     * @param action Type of event.
     * @param payload Data required to process the event. Complex data can be represented
     *      in JSON string.
     */
    void onReceivedEvent(const std::string& action, const std::string& payload);

private:
    /**
     * Constructor for @c LocationProviderHandler.
     */
    LocationProviderHandler(
        std::shared_ptr<aasb::core::logger::LoggerHandler> logger,
        std::shared_ptr<aasb::bridge::IConfigurationProvider> config,
        std::weak_ptr<aasb::bridge::ResponseDispatcher> responseDispatcher);

    /**
     * When AASB client receives message to provide location, at some point
     * of time, it should respond with the current location. This handler
     * is invoked by AASB Controller to notify and wake up the thread waiting
     * on @c getLocation
     *
     * @param payload Json containing location made available by AASB client.
     */
    void onLocationReceived(const std::string& payload);

    // aasb::core::logger::LoggerHandler
    std::shared_ptr<aasb::core::logger::LoggerHandler> m_logger;

    // For obtaining location and country from config.
    std::shared_ptr<aasb::bridge::IConfigurationProvider> m_config;

    // To send directive to service
    std::weak_ptr<aasb::bridge::ResponseDispatcher> m_responseDispatcher;

    // Apparatus for fetching location (sync over async)
    std::condition_variable m_cv_location;
    std::mutex m_mutex_location;
    bool m_location_set;

    // Cached location
    aace::location::Location m_current_location;
};

}  // namespace location
}  // namespace aasb

#endif  // AASB_LOCATION_LOCATIONPROVIDER_HANDLER_H
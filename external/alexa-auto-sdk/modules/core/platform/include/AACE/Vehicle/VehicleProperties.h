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

#ifndef AACE_VEHICLE_PROPERTY_VEHICLE_PROPERTIES_H
#define AACE_VEHICLE_PROPERTY_VEHICLE_PROPERTIES_H

/** @file */

namespace aace {
namespace vehicle {
namespace property {

/**
 * This property is used with Engine::setProperty() to change the current operating country. The value
 * must be a valid 2-letter ISO country code.
 */
static const std::string OPERATING_COUNTRY = "aace.vehicle.operatingCountry";

} // aace::vehicle::property
} // aace::vehicle
} // aace

#endif // AACE_VEHICLE_PROPERTY_VEHICLE_PROPERTIES_H

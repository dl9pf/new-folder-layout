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
#include "CBLHandler.h"
#include <fstream>
#include <iostream>
#include <rapidjson/document.h>
#include <rapidjson/prettywriter.h>
#include <rapidjson/stringbuffer.h>

#include <aasb/Consts.h>
#include "ResponseDispatcher.h"
#include "PlatformSpecificLoggingMacros.h"

/**
 * Specifies the severity level of a log message
 * @sa @c aace::logger::LoggerEngineInterface::Level
 */
using Level = aace::logger::LoggerEngineInterface::Level;

namespace aasb {
namespace cbl {

using namespace rapidjson;

const std::string TAG = "aasb::alexa::CBLHandler";

std::shared_ptr<CBLHandler> CBLHandler::create(
    std::shared_ptr<aasb::core::logger::LoggerHandler> logger,
    std::weak_ptr<aasb::bridge::ResponseDispatcher> responseDispatcher,
    std::string refresh_token_file) {
    auto cblHandler = std::shared_ptr<CBLHandler>(new CBLHandler(logger, responseDispatcher, refresh_token_file));
    return cblHandler;
}

CBLHandler::CBLHandler(std::shared_ptr<aasb::core::logger::LoggerHandler> logger,
                       std::weak_ptr<aasb::bridge::ResponseDispatcher> responseDispatcher,
                       std::string refresh_token_file) :
        m_logger(logger), m_responseDispatcher(responseDispatcher), m_refresh_token_file(refresh_token_file) {
    loadRefreshTokenFromFile();
}

void CBLHandler::loadRefreshTokenFromFile() {
    std::ifstream file(m_refresh_token_file, std::ifstream::in);
    if(file.is_open()) {
        m_RefreshToken = std::string((std::istreambuf_iterator<char>(file)),
                                     (std::istreambuf_iterator<char>()));
        file.close();
    } else {
        m_RefreshToken = "";
        m_logger->log(Level::WARN, TAG, "loadRefreshTokenFromFile: Error opening refresh token file");
    }
}

void CBLHandler::cblStateChanged(CBLState state, CBLStateChangedReason reason, const std::string& url, const std::string& code) {
    std::string info = std::string(": CBLState: ") + std::string(convertCBLStateToString(state)) +
                       std::string(": CBLStateChangedReason: ") +
                       std::string(convertCBLStateChangedReasonToString(reason) +
                       std::string(": url: ") + url +
                       std::string(": code: ") + code);

    m_logger->log(Level::INFO, TAG, info);

    if (auto responseDispatcher = m_responseDispatcher.lock()) {
        if (state == CBLState::CODE_PAIR_RECEIVED) {
            rapidjson::Document document;
            document.SetObject();
            rapidjson::Value payloadElement;

            payloadElement.SetObject();
            payloadElement.AddMember("url", rapidjson::Value().SetString(url.c_str(), url.length()), document.GetAllocator());
            payloadElement.AddMember("code", rapidjson::Value().SetString(code.c_str(), code.length()), document.GetAllocator());

            document.AddMember("payload", payloadElement, document.GetAllocator());

            // create event string
            rapidjson::StringBuffer buffer;
            rapidjson::PrettyWriter<rapidjson::StringBuffer> writer( buffer );

            document.Accept( writer );

            responseDispatcher->sendDirective(
                aasb::bridge::TOPIC_CBL,
                aasb::bridge::ACTION_CBL_CODEPAIR_RECEIVED,
                buffer.GetString());
        } else if ((state == CBLState::STOPPING) && (reason == CBLStateChangedReason::CODE_PAIR_EXPIRED)) {
            m_logger->log(Level::WARN, TAG, "The code has expired. Retry to generate a new code.");
            responseDispatcher->sendDirective(
                aasb::bridge::TOPIC_CBL,
                aasb::bridge::ACTION_CBL_CODEPAIR_EXPIRED,
                "");
        }
    }
}

void CBLHandler::clearRefreshToken() {
    m_logger->log(Level::VERBOSE, TAG, "clearRefreshToken");

    clearRefreshTokenInternal();
}

void CBLHandler::setRefreshToken(const std::string& refreshToken) {
    m_logger->log(Level::VERBOSE, TAG, "setRefreshToken");

    setRefreshTokenInternal(refreshToken);
}

std::string CBLHandler::getRefreshToken() {
    m_logger->log(Level::VERBOSE, TAG, "getRefreshToken");

    // Return available refresh token
    return getRefreshTokenInternal();
}

void CBLHandler::setUserProfile(const std::string& name, const std::string& email) {
    m_logger->log(Level::VERBOSE, TAG, "setUserProfile" + name + "email" + email);
    if (auto responseDispatcher = m_responseDispatcher.lock()) {
        if (!name.empty()) {
            responseDispatcher->sendDirective(
                    aasb::bridge::TOPIC_CBL,
                    aasb::bridge::ACTION_CBL_SET_PROFILE_NAME,
                    name);
        }
    }
}

void CBLHandler::onReceivedEvent(const std::string& action, const std::string& payload) {
    m_logger->log(Level::INFO, TAG, "onReceivedEvent: " + action);

    if (action == aasb::bridge::ACTION_CBL_START) {
        start();
    } else if (action == aasb::bridge::ACTION_CBL_CANCEL) {
        cancel();
    } else {
        AASB_ERROR("CBLHandler: action %s is unknown.", action.c_str());
    }

    return;
}

std::string CBLHandler::convertCBLStateToString(CBLState state) {
    switch (state) {
        case CBLState::STARTING:
            return "STARTING";
        case CBLState::REQUESTING_CODE_PAIR:
            return "REQUESTING_CODE_PAIR";
        case CBLState::CODE_PAIR_RECEIVED:
            return "CODE_PAIR_RECEIVED";
        case CBLState::REFRESHING_TOKEN:
            return "REFRESHING_TOKEN";
        case CBLState::REQUESTING_TOKEN:
            return "REQUESTING_TOKEN";
        case CBLState::STOPPING:
            return "STOPPING";
        default:
            return std::string("UNKNOWN");
    }
}

std::string CBLHandler::convertCBLStateChangedReasonToString(CBLStateChangedReason reason) {
    switch (reason) {
        case CBLStateChangedReason::SUCCESS:
            return "SUCCESS";
        case CBLStateChangedReason::ERROR:
            return "ERROR";
        case CBLStateChangedReason::TIMEOUT:
            return "TIMEOUT";
        case CBLStateChangedReason::CODE_PAIR_EXPIRED:
            return "CODE_PAIR_EXPIRED";
        case CBLStateChangedReason::NONE:
            return "NONE";
        default:
            return std::string("UNKNOWN");
    }
}

void CBLHandler::setRefreshTokenInternal(const std::string& refreshToken) {
    std::lock_guard<std::mutex> lock(m_mutex);

    m_RefreshToken = refreshToken;

    std::ofstream file(m_refresh_token_file, std::ofstream::trunc);
    if(file.is_open()) {
        file << refreshToken;
        file.close();
    } else {
        m_logger->log(Level::ERROR, TAG, "setRefreshTokenInternal: Error opening refresh token file");
    }
}

std::string CBLHandler::getRefreshTokenInternal() {
    std::lock_guard<std::mutex> lock(m_mutex);
    return m_RefreshToken;
}

void CBLHandler::clearRefreshTokenInternal() {
    std::lock_guard<std::mutex> lock(m_mutex);

    m_RefreshToken.clear();

    // Remove refresh token file
    if(remove(m_refresh_token_file.c_str()) != 0) {
        m_logger->log(Level::ERROR, TAG, "clearRefreshTokenInternal: Error clearing refresh token file");
    }
}

}  // namespace cbl
}  // namespace aasb
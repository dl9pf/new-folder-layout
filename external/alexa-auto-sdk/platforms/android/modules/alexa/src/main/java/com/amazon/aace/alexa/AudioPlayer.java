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

// aace/alexa/AudioPlayer.java
// This is an automatically generated file.

package com.amazon.aace.alexa;

import com.amazon.aace.core.PlatformInterface;

/**
 * AudioPlayer should be extended to handle audio output from the Engine.
 *
 * @note Audio playback control operations such as on-platform button presses must be routed through
 * the @c PlaybackController.
 *
 *
 * @sa PlaybackController
 *
 * @sa TemplateRuntime.renderPlayerInfo()
 */
public class AudioPlayer extends PlatformInterface
{
    /**
     * Specifies the state of audio playback activity
     */
    public enum PlayerActivity
    {
        /**
         * Audio playback has not yet begun.
         * @hideinitializer
         */
        IDLE("IDLE"),

        /**
         * Audio is currently playing.
         * @hideinitializer
         */
        PLAYING("PLAYING"),

        /**
         * Audio playback is stopped, either from a stop directive or playback error.
         * @hideinitializer
         */
        STOPPED("STOPPED"),

        /**
         * Audio playback is paused.
         * @hideinitializer
         */
        PAUSED("PAUSED"),

        /**
         * Audio playback is stalled because a buffer underrun has occurred.
         * @hideinitializer
         */
        BUFFER_UNDERRUN("BUFFER_UNDERRUN"),

        /**
         * Audio playback is finished.
         * @hideinitializer
         */
        FINISHED("FINISHED");

        /**
         * @internal
         */
        private String m_name;

        /**
         * @internal
         */
        private PlayerActivity( String name ) {
            m_name = name;
        }

        /**
         * @internal
         */
        public String toString() {
            return m_name;
        }
    }

    /**
     * Notifies the platform implementation of a change in audio playback state
     *
     * @param  state The new playback state
     */
    public void playerActivityChanged( PlayerActivity state ) {
    }

    // NativeRef implementation
    final protected long createNativeRef() {
        return createBinder();
    }

    final protected void disposeNativeRef( long nativeRef ) {
        disposeBinder( nativeRef );
    }

    // Native Engine JNI methods
    private native long createBinder();
    private native void disposeBinder( long nativeRef );
}



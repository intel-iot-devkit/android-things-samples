/*
 * Copyright (c) 2017 Intel Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.upm.androidthings.driverlibrary.BoardDefaults;
import mraa.mraa;

public class GroveRelay extends Activity {

    private static final String TAG = "GroveRelayActivity";

    static {
        try {
            System.loadLibrary("javaupm_grove");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native code library failed to load." +  e);
            System.exit(1);
        }
    }

    upm_grove.GroveRelay relay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_relay);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int gpioIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Relay_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Relay_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Relay_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        relay = new upm_grove.GroveRelay(gpioIndex);
        relayTask.run();

    }

    Runnable relayTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            try {
                for (int i = 0; i < 6; i++) {
                    relay.on();
                    if (relay.isOn())
                        Log.i(TAG, "Relay is on");
                    Thread.sleep(1000);

                    relay.off();
                    if (relay.isOff())
                        Log.i(TAG, "Relay is off");
                    Thread.sleep(1000);
                }

                relay.off();
                relay.delete();

            } catch (InterruptedException e) {
                e.printStackTrace();
                // TODO: throw the exception up the stack or exit.
            }
        }
    };
}

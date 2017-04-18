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

public class GroveButton extends Activity {
    private static final String TAG = "GroveButtonActivity";

    static {
        try {
            System.loadLibrary("javaupm_grove");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native code library failed to load." +  e);
            System.exit(1);
        }
    }

    upm_grove.GroveButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_button);

        int gpioIndex = -1;
        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Button_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Button_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Button_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        button = new upm_grove.GroveButton(gpioIndex);
        buttonTask.run();
    }

    Runnable buttonTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            try {

                while (true) {
                    Log.i(TAG, button.name() + " value is " + button.value());
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                // TODO: throw the exception up the stack or exit.
            }
        }
    };
}

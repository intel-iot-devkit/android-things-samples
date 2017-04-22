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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.upm.androidthings.driverlibrary.BoardDefaults;
import mraa.mraa;

public class GroveTouch extends Activity {

    private static final String TAG = "GroveTouchActivity";

    upm_ttp223.TTP223 touch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_touch);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int gpioIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Touch_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Touch_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Touch_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        touch = new upm_ttp223.TTP223(gpioIndex);
        AsyncTask.execute(touchSensorTask);
    }

    Runnable touchSensorTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            try {
                while (true) {
                    if (touch.isPressed())
                        Log.i(TAG, touch.name() + " is pressed");
                    else
                        Log.i(TAG, touch.name() + " is not pressed");

                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                touch.delete();
                GroveTouch.this.finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Thread.currentThread().interrupt();
        touch.delete();
    }
}

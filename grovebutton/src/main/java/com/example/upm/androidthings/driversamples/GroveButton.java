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
import android.widget.TextView;

import com.example.upm.androidthings.driversupport.BoardDefaults;

import mraa.mraa;

public class GroveButton extends Activity {
    private static final String TAG = "GroveButtonActivity";

    upm_grove.GroveButton button;
    TextView tv;
    Runnable buttonTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            int i = 1; // iteration counter to defeat the chatty detector in Log.i
            try {
                while (true) {
                    updateUI(i++, button.value());
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                button.delete();
                GroveButton.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_button);

        int gpioIndex = -1;
        tv = (TextView) findViewById(R.id.text_value);
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
        AsyncTask.execute(buttonTask);
    }

    private void updateUI(int i, int bv) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(button.name() + " value is " + bv);
                Log.i(TAG, "iteration: " + i + ", " + button.name() + " value is " + bv);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "in onDestroy() call");
        Thread.currentThread().interrupt();
        button.delete();
    }
}

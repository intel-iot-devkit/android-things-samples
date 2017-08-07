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

public class RangeFinderActivity extends Activity {
    private static final String TAG = "RangeFinderActivity";

    upm_urm37.URM37 RangeFinder;
    TextView tv, tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_finder);
        tv = (TextView) findViewById(R.id.text_value);
        tv1 = (TextView) findViewById(R.id.textView);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int uartIndex = -1;
        int gpioIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                uartIndex = mraa.getUartLookup(getString(R.string.UART_Edison_Arduino));
                gpioIndex = mraa.getGpioLookup(getString(R.string.Reset_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                uartIndex = mraa.getUartLookup(getString(R.string.UART_Edison_Sparkfun));
                gpioIndex = mraa.getGpioLookup(getString(R.string.Reset_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                uartIndex = mraa.getUartLookup(getString(R.string.UART_Joule_Tuchuck));
                gpioIndex = mraa.getGpioLookup(getString(R.string.Reset_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        Log.i(TAG, "uartIndex: " + uartIndex);
        Log.i(TAG, "gpioIndex: " + gpioIndex);

        RangeFinder = new upm_urm37.URM37(uartIndex, gpioIndex);
        //RangeFinder.getTemperature();
        AsyncTask.execute(RangeFinderTask);
    }

    Runnable RangeFinderTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            try {
                while (true) {
                    //updateUI(RangeFinder.getDistance(), RangeFinder.getTemperature());
                    updateUI(RangeFinder.getTemperature());
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                RangeFinder.delete();
                RangeFinderActivity.this.finish();
            }
        }
    };

    /* private void updateUI(float dist, float tmp) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText("Distance" + "     " + dist);
                tv1.setText("Temperature" + "     " + tmp);
                Log.i(TAG, "Distance: " + dist + ", " + "Temperature: " + tmp + " °C");
            }
        });
    }*/

    private void updateUI(float tmp) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //tv.setText("Distance" + "     " + dist);
                tv1.setText("Temperature" + "     " + tmp);
                //Log.i(TAG, "Distance: " + dist + ", " + "Temperature: " + tmp + " °C");
                Log.i(TAG, "Temperature: " + tmp + " °C");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "in onDestroy() call");
        Thread.currentThread().interrupt();
        RangeFinder.delete();
    }
}

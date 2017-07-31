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

public class MHZActivity extends Activity {
    private static final String TAG = "MHZActivity";

    upm_mhz16.MHZ16 mhz16;
    TextView tv, tv1;
    Runnable co2Task = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            int i = 1; // iteration counter to defeat the chatty detector in Log.i

            if (mhz16.setupTty())
            {
                try {
                    while (true) {
                        if (!mhz16.getData()) {
                            System.out.println("Failed to retrieve data");
                            continue;
                        }
                        updateUI(i++, mhz16.getGas(), mhz16.getTemperature());

                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    mhz16.delete();
                    MHZActivity.this.finish();
                }
        } else {
                MHZActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhz);
        Log.d(TAG, "Starting MHZActivity");

        int uartIndex = -1;
        tv = (TextView) findViewById(R.id.text_value);
        tv1 = (TextView) findViewById(R.id.textView);
        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                uartIndex = mraa.getUartLookup(getString(R.string.UART_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                uartIndex = mraa.getUartLookup(getString(R.string.UART_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                uartIndex = mraa.getUartLookup(getString(R.string.UART_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        mhz16 = new upm_mhz16.MHZ16(uartIndex);
        AsyncTask.execute(co2Task);
    }

    private void updateUI(int i, int gas, int Temp) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText("Gas    " + gas);
                tv1.setText("Temp   " + Temp);
                Log.i(TAG, "iteration: " + i + ", " + "Gas value is " + gas + "Temp Value is" + Temp);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "in onDestroy() call");
        Thread.currentThread().interrupt();
        mhz16.delete();
    }
}

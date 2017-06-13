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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.upm.androidthings.driversupport.BoardDefaults;
import mraa.mraa;

public class GroveLed extends Activity {
    private static final String TAG = "GroveLED";

    private boolean ledStatus;
    TextView tv;

    upm_grove.GroveLed led;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_led);

        tv = (TextView)findViewById(R.id.activity_led_status);
        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int gpioIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Led_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Led_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Led_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        led = new upm_grove.GroveLed(gpioIndex);
        AsyncTask.execute(ledTask);
    }

    Runnable ledTask = new Runnable() {

        @Override
        public void run(){
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            try {
                for (int i = 0; i < 10; ++i) {
                    if (led.on() == 0){
                        ledStatus = true;
                    }
                    updateUI(ledStatus);
                    Thread.sleep(1000);

                    if(led.off() == 0){
                        ledStatus = false;
                    }
                    updateUI(ledStatus);
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                led.off();
                led.delete();
                GroveLed.this.finish();
            }
        }
    };

    private void updateUI(boolean Status) {
        this.runOnUiThread(new Runnable() {
            @Override
                public void run() {
                    if (Status == true) {
                        tv.setText("LED Status is On");
                        Log.d(TAG, "Status on");
                    } else {
                        tv.setText("LED Status is Off");
                        Log.d(TAG, "Status off");
                    }
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "in onDestroy() call");
        Thread.currentThread().interrupt();
        led.delete();
    }
}

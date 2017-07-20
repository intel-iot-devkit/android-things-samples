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

public class BMETempActivity extends Activity {
    private static final String TAG = "BMETempActivity";

    upm_bmp280.BME280 tphSensor;
    TextView tv, tv2, tv3, tv4, tv5;
    Runnable tphSensorTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            int i = 1; // iteration counter to defeat the chatty detector in Log.i
            float[] SensorValues = new float[5];
            try {
                while (true) {
                    // update our values from the sensor
                    tphSensor.update();
                    SensorValues[0] = tphSensor.getTemperature();
                    SensorValues[1] = tphSensor.getTemperature(true);
                    SensorValues[2] = tphSensor.getPressure();
                    SensorValues[3] = tphSensor.getAltitude();
                    SensorValues[4] = tphSensor.getHumidity();

                    updateUI(i++, SensorValues);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                tphSensor.reset();
                BMETempActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmetemp);
        tv = (TextView) findViewById(R.id.text_value);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv4 = (TextView) findViewById(R.id.textView4);
        tv5 = (TextView) findViewById(R.id.textView5);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int i2cIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Bme_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Bme_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Bme_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        tphSensor = new upm_bmp280.BME280(i2cIndex);
        AsyncTask.execute(tphSensorTask);
    }

    private void updateUI(int i, float[] SensorValues) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText("Temperature    " + SensorValues[0] + "C");
                tv2.setText("Temperature    " + SensorValues[1] + "F");
                tv3.setText("Pressure    " + SensorValues[2] + "Pa");
                tv4.setText("Altitude    " + SensorValues[3] + "m");
                tv5.setText("Humidity    " + SensorValues[4] + "%RH");

                Log.i(TAG, "iteration: " + i + ", " + "Compensation Temperature: " + SensorValues[0]
                        + " C / "
                        + SensorValues[1]
                        + " F");
                Log.i(TAG, "iteration: " + i + ", " + "Pressure: "
                        + SensorValues[2]
                        + " Pa");

                Log.i(TAG, "iteration: " + i + ", " + "Computed Altitude: "
                        + SensorValues[3]
                        + " m");

                Log.i(TAG, "iteration: " + i + ", " + "Humidity: "
                        + SensorValues[4]
                        + " %RH");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Thread.currentThread().interrupt();
        tphSensor.reset();
    }
}

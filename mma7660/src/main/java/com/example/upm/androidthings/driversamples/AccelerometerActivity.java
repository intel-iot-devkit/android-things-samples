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
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.upm.androidthings.driverlibrary.Mma7660AccelerometerDriver;
import com.example.upm.androidthings.driversupport.BoardDefaults;

import java.io.IOException;

import mraa.mraa;

/**
 * AccelerometerActivity is a sample activity that uses an Accelerometer driver to
 * read data from a Grove accelerator and log them.
 */
public class AccelerometerActivity extends Activity implements SensorEventListener {
    private static final String TAG = AccelerometerActivity.class.getSimpleName();

    TextView tv, tv1, tv2;
    private Mma7660AccelerometerDriver mAccelerometerDriver;
    private SensorManager mSensorManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        tv = (TextView) findViewById(R.id.text_value);
        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);

        Log.i(TAG, "Accelerometer demo created");

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int i2cIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Accel_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Accel_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Accel_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerDynamicSensorCallback(new SensorManager.DynamicSensorCallback() {
            @Override
            public void onDynamicSensorConnected(Sensor sensor) {
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    Log.i(TAG, "Accelerometer sensor connected");
                    mSensorManager.registerListener(AccelerometerActivity.this, sensor,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        });
        try {
            mAccelerometerDriver = new Mma7660AccelerometerDriver(i2cIndex);
            mAccelerometerDriver.register();
            Log.i(TAG, "Accelerometer driver registered");
        } catch (IOException e) {
            Log.e(TAG, "Error initializing accelerometer driver: ", e);
            AccelerometerActivity.this.finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAccelerometerDriver != null) {
            mSensorManager.unregisterListener(this);
            mAccelerometerDriver.unregister();
            try {
                mAccelerometerDriver.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing accelerometer driver: ", e);
            } finally {
                mAccelerometerDriver = null;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        updateUI(event.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(TAG, "Accelerometer accuracy changed: " + accuracy);
    }

    private void updateUI(float[] values) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText("event.Values[0]     " + values[0]);
                tv1.setText("event.Values[1]     " + values[1]);
                tv2.setText("event.Values[2]     " + values[2]);

                Log.i(TAG, "Accelerometer event: " +
                        values[0] + ", " + values[1] + ", " + values[2]);
            }
        });
    }
}

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
import android.os.Bundle;
import android.util.Log;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import mraa.mraa;

import java.io.IOException;

/**
  * Example of using MMA7660 and Grove Jhd1313m1 LCD.
  *
  * This activity initializes the LCD and displays Acceleromter (x,y,z) in different color combinations.
  *
  */

public class DispAccelActivity extends Activity implements SensorEventListener {
    private static String TAG = "DispAccelActivity";
    static {
        try {
            System.loadLibrary("javaupm_mma7660");
            System.loadLibrary("javaupm_jhd1313m1");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native library failed to load." + e);
            System.exit(1);
        }
    }

    private Mma7660AccelerometerDriver mAccelerometerDriver;
    private SensorManager mSensorManager;
    private upm_jhd1313m1.Jhd1313m1 lcd;
    private int ndx = 0;
    private short[][] rgb = new short[][]{
                {0xd1, 0x00, 0x00},     // red
                {0xff, 0x66, 0x22},     // orange
                {0xff, 0xda, 0x21},     // yellow
                {0x33, 0xdd, 0x00},     // green
                {0x11, 0x33, 0xcc},     // blue
                {0x22, 0x00, 0x66},     // violet
                {0x33, 0x00, 0x44}};    // darker violet
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting DispAccelActivity");

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int i2cIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Joule_Tuchuck));
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
                    mSensorManager.registerListener(DispAccelActivity.this, sensor,
                            SensorManager.SENSOR_DELAY_NORMAL);
                }
            }
        });

        try {
            mAccelerometerDriver = new Mma7660AccelerometerDriver(i2cIndex);
            mAccelerometerDriver.register();
            Log.i(TAG, "Accelerometer driver registered");
            lcd = new upm_jhd1313m1.Jhd1313m1(i2cIndex);
            lcd.clear();
            Log.i(TAG, "Display initialized");
        } catch (IOException e) {
            Log.e(TAG, "Error initializing drivers: ", e);
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
                if (lcd != null) {
                    lcd.delete();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing accelerometer driver: ", e);
            } finally {
                mAccelerometerDriver = null;
                lcd = null;
            }
        }
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i(TAG, "Accelerometer event: " +
                event.values[0] + ", " + event.values[1] + ", " + event.values[2]);

        try {
            // Display text and read count in first row
            lcd.setCursor(0, 0);

            // Change the color
            short r = rgb[ndx % 7][0];
            short g = rgb[ndx % 7][1];
            short b = rgb[ndx % 7][2];
            lcd.setColor(r, g, b);
            lcd.write("Accel(x,y,z) " + ndx++%1000);

            // Display coordinates in second row
            lcd.setCursor(1, 0);
            lcd.write(String.format("%.2f ", event.values[0]) +
                       String.format("%.2f ", event.values[1]) +
                        String.format("%.2f ", event.values[2]));
            Thread.sleep(1000);

        // shouldn't catch an unqualified exception. Should exit in any case.
        } catch (Exception e) {
            Log.e(TAG, "Error in Callback", e);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(TAG, "Accelerometer accuracy changed: " + accuracy);
    }
}

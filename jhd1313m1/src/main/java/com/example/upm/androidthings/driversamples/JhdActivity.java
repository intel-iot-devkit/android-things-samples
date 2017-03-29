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
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.example.upm.androidthings.driverlibrary.BoardDefaults;

import mraa.mraa;

/**
  * Example of using Grove Jhd1313m1 LCD.
  *
  * This activity initializes the LCD and displays RGB color combination with text.
  *
  */

public class JhdActivity extends Activity {
    private static final String TAG = "JhdActivity";

    private upm_jhd1313m1.Jhd1313m1 lcd;
    private HandlerThread mDisplayThread;

    static {
        try {
            System.loadLibrary("javaupm_jhd1313m1");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native library failed to load." + e);
            System.exit(1);
        }
    }

    private Runnable mDisplayLoop = this::displayOnLcd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Starting JhdActivity");

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int i2cIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Lcd_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Lcd_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Lcd_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        mDisplayThread = new HandlerThread("Display Thread");
        mDisplayThread.start();
        Handler mDisplayHandler = new Handler(mDisplayThread.getLooper());

        lcd = new upm_jhd1313m1.Jhd1313m1(i2cIndex);
        mDisplayHandler.post(mDisplayLoop);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Display Loop Destroyed");

        if (mDisplayThread != null) {
            mDisplayThread.quitSafely();
        }
        lcd.delete();
    }

    private void displayOnLcd() {
        if (lcd != null ) {
            lcd.clear();

            int ndx = 0;
            short[][] rgb = new short[][]{
                    {0xd1, 0x00, 0x00},   // red
                    {0xff, 0x66, 0x22},   // orange
                    {0xff, 0xda, 0x21},   // yellow
                    {0x33, 0xdd, 0x00},   // green
                    {0x11, 0x33, 0xcc},   // blue
                    {0x22, 0x00, 0x66},   // violet
                    {0x33, 0x00, 0x44}};  // darker violet

            while(true) {
                try {
                    // Alternate rows on the LCD
                    lcd.setCursor(ndx % 2, 0);

                    // Change the color
                    short r = rgb[ndx % 7][0];
                    short g = rgb[ndx % 7][1];
                    short b = rgb[ndx % 7][2];
                    lcd.setColor(r, g, b);

                    lcd.write("Hello World " + ndx);

                    // Echo via printf
                    Log.d(TAG, "Hello World" + ndx++);
                    Log.d(TAG, String.format("rgb: 0x%02x%02x%02x", r, g, b));

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally{
                    mDisplayThread.quitSafely();
                }
            }
        } else{
            JhdActivity.this.finish();
        }
    }
}
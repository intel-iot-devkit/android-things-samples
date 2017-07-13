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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.upm.androidthings.driversupport.BoardDefaults;

import mraa.mraa;

public class MultiSensorActivity extends Activity {
    private static final String TAG = "MultiSensorActivity";

    short[][] rgb = new short[][]{
            {0xd1, 0x00, 0x00},   // red
            {0x33, 0xdd, 0x00},   // green
            {0x11, 0x33, 0xcc}};   // blue
    TextView tv;
    ImageView im1;
    ImageView im2;
    ImageView im3;

    upm_tmp006.TMP006 thermopile;
    upm_jhd1313m1.Jhd1313m1 lcd;
    upm_grove.GroveLed Redled;
    upm_grove.GroveButton button;
    upm_grove.GroveLed Blueled;
    upm_ttp223.TTP223 touch;
    upm_grove.GroveLed Greenled;

    float Temp = 0.0f;
    private int ndx = 0;
    Runnable thermopileTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            thermopile.setActive();
            try {
                if (lcd != null) {
                    lcd.clear();
                }
                while (true) {
                    // Print out temperature value in °C
                    Temp = thermopile.getTemperature(1);
                    Log.i(TAG, "Temperature: " + Temp + " °C");

                    updateUI();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lcd.delete();
                thermopile.delete();
                Redled.off();
                Redled.delete();
                button.delete();
                Blueled.off();
                Blueled.delete();
                touch.delete();
                Greenled.off();
                Greenled.delete();
                MultiSensorActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_sensor);
        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());

        int i2cIndex = -1;
        int gpioRedLedIndex = -1;
        int gpioButtonIndex = -1;
        int gpioBlueLedIndex = -1;
        int gpioTouchIndex = -1;
        int gpioGreenLedIndex = -1;

        tv = (TextView) findViewById(R.id.activity_Temp_Values);
        im1 = (ImageView) findViewById(R.id.imageView);
        im2 = (ImageView) findViewById(R.id.imageView2);
        im3 = (ImageView) findViewById(R.id.imageView3);

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                i2cIndex = mraa.getI2cLookup(getString(R.string.I2C_Edison_Arduino));
                gpioRedLedIndex = mraa.getGpioLookup(getString(R.string.LedRed_Edison_Arduino));
                gpioButtonIndex = mraa.getGpioLookup(getString(R.string.Button_Edison_Arduino));
                gpioBlueLedIndex = mraa.getGpioLookup(getString(R.string.LedBlue_Edison_Arduino));
                gpioTouchIndex = mraa.getGpioLookup(getString(R.string.Touch_Edison_Arduino));
                gpioGreenLedIndex = mraa.getGpioLookup(getString(R.string.LedGreen_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                i2cIndex = mraa.getI2cLookup(getString(R.string.I2C_Edison_Sparkfun));
                gpioRedLedIndex = mraa.getGpioLookup(getString(R.string.LedRed_Edison_Sparkfun));
                gpioButtonIndex = mraa.getGpioLookup(getString(R.string.Button_Edison_Sparkfun));
                gpioBlueLedIndex = mraa.getGpioLookup(getString(R.string.LedBlue_Edison_Arduino));
                gpioTouchIndex = mraa.getGpioLookup(getString(R.string.Touch_Edison_Sparkfun));
                gpioGreenLedIndex = mraa.getGpioLookup(getString(R.string.LedGreen_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                i2cIndex = mraa.getI2cLookup(getString(R.string.I2C_Joule_Tuchuck));
                gpioRedLedIndex = mraa.getGpioLookup(getString(R.string.LedRed_Joule_Tuchuck));
                gpioButtonIndex = mraa.getGpioLookup(getString(R.string.Button_Joule_Tuchuck));
                gpioBlueLedIndex = mraa.getGpioLookup(getString(R.string.LedBlue_Joule_Tuchuck));
                gpioTouchIndex = mraa.getGpioLookup(getString(R.string.Touch_Joule_Tuchuck));
                gpioGreenLedIndex = mraa.getGpioLookup(getString(R.string.LedGreen_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        thermopile = new upm_tmp006.TMP006(i2cIndex, (short) 0, 64);
        lcd = new upm_jhd1313m1.Jhd1313m1(i2cIndex);
        Redled = new upm_grove.GroveLed(gpioRedLedIndex);
        button = new upm_grove.GroveButton(gpioButtonIndex);
        Blueled = new upm_grove.GroveLed(gpioBlueLedIndex);
        touch = new upm_ttp223.TTP223(gpioTouchIndex);
        Greenled = new upm_grove.GroveLed(gpioGreenLedIndex);
        AsyncTask.execute(thermopileTask);
    }

    private void updateUI() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText("Temp values" + "    " + Float.toString(Temp));

                // Alternate rows on the LCD
                lcd.setCursor(ndx % 2, 0);

                // Change the color
                short r = rgb[ndx % 3][0];
                short g = rgb[ndx % 3][1];
                short b = rgb[ndx % 3][2];
                lcd.setColor(r, g, b);

                lcd.write("Temp" + "       " + Temp + " °C");
                ndx++;

                if (button.value() == 0) {
                    Blueled.off();
                    im3.setImageResource(R.drawable.blue_off);
                } else {
                    Blueled.on();
                    im3.setImageResource(R.drawable.blue_on);
                }

                if (Temp >= 27) {
                    Redled.on();
                    im2.setImageResource(R.drawable.red_on);
                } else {
                    Redled.off();
                    im2.setImageResource(R.drawable.red_off);
                }

                if (touch.isPressed()) {
                    Greenled.on();
                    im1.setImageResource(R.drawable.green_on);
                } else {
                    Greenled.off();
                    im1.setImageResource(R.drawable.green_off);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Thread.currentThread().interrupt();
        lcd.delete();
        thermopile.delete();
        Redled.delete();
        button.delete();
        Blueled.delete();
        touch.delete();
        Greenled.delete();
    }
}

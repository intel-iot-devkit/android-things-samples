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
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.example.upm.androidthings.driversupport.BoardDefaults;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import mraa.mraa;

public class ADCActivity extends Activity {
    private static final String TAG = "ADCActivity";

    static int id = 0; // Sample number
    File filename = new File(Environment.getExternalStorageDirectory() + "/filename.txt");
    upm_ads1x15.ADS1015 adc;

    FileWriter fw;
    BufferedWriter bw;

    TextView tv;
    Runnable adcSensorTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            float lastSample;
            int i = 0;
            try {
                fw = new FileWriter(filename);
                bw = new BufferedWriter(fw);
            } catch (IOException e) {
                Log.e(TAG, "Failed to open output file for writing: ", e);
                System.exit(1);
            }
            try {
                for (i = 0; i < 10; i++) {
                    lastSample = adc.getLastSample();
                    bw.write(id + " " + String.format("%.7f", lastSample) + "\n");
                    updateUI(lastSample);
                    id++;
                    Thread.sleep(1000);
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed to open output file for writing: ", e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                try {
                    if (bw) {
                        bw.close();
                    }
                    if (fw) {
                        fw.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Failed to close output file");
                    e.printStackTrace();
                }
                adc.delete();
                ADCActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adc);
        tv = (TextView) findViewById(R.id.text_value);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int i2cIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                i2cIndex = mraa.getI2cLookup(getString(R.string.ADC_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                i2cIndex = mraa.getI2cLookup(getString(R.string.ADC_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                i2cIndex = mraa.getI2cLookup(getString(R.string.ADC_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        adc = new upm_ads1x15.ADS1015(i2cIndex, (short) 0x48);
        // Put the ADC into differential mode for pins A0 and A1
        adc.getSample(upm_ads1x15.ADS1X15.ADSMUXMODE.DIFF_0_1);

        // Set the gain based on expected VIN range to -/+ 2.048 V
        // Can be adjusted based on application to as low as -/+ 0.256 V, see API
        // documentation for details
        adc.setGain(upm_ads1x15.ADS1X15.ADSGAIN.GAIN_TWO);

        // Set the sample rate to 3300 samples per second (max) and turn on continuous
        // sampling
        adc.setSPS(upm_ads1x15.ADS1015.ADSSAMPLERATE.SPS_3300);
        adc.setContinuous(true);

        AsyncTask.execute(adcSensorTask);
    }

    private void updateUI(float lastSample) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(adc.name() + "   Conversion register values without performing a conversion operation is " + lastSample);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Thread.currentThread().interrupt();
        adc.delete();
    }
}

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

public class GroveBuzzer extends Activity {
    private static final String TAG = "GroveBuzzer";

    upm_buzzer.Buzzer sound;
    TextView tv;
    Runnable soundTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            int chord[] = {
                    upm_buzzer.javaupm_buzzer.BUZZER_DO,
                    upm_buzzer.javaupm_buzzer.BUZZER_RE,
                    upm_buzzer.javaupm_buzzer.BUZZER_MI,
                    upm_buzzer.javaupm_buzzer.BUZZER_FA,
                    upm_buzzer.javaupm_buzzer.BUZZER_SOL,
                    upm_buzzer.javaupm_buzzer.BUZZER_LA,
                    upm_buzzer.javaupm_buzzer.BUZZER_SI};
            try {
                for (int i = 0; i < chord.length; i++) {
                    // play each note for one half second
                    int note = sound.playSound(chord[i], 500000);
                    updateUI("Buzzer Buzzing");

                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                sound.stopSound();
                GroveBuzzer.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_buzzer);
        tv = (TextView) findViewById(R.id.text_value);

        int pwmIndex = -1;
        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                pwmIndex = mraa.getPwmLookup(getString(R.string.Buzzer_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                pwmIndex = mraa.getPwmLookup(getString(R.string.Buzzer_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                pwmIndex = mraa.getPwmLookup(getString(R.string.Buzzer_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        // Instantiate a buzzer on digital pin D5
        sound = new upm_buzzer.Buzzer(pwmIndex);
        AsyncTask.execute(soundTask);
    }

    private void updateUI(String s) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(s);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "in onDestroy() call");
        Thread.currentThread().interrupt();
        //button.delete();
    }
}


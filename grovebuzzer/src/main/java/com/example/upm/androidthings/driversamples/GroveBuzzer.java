package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.upm.androidthings.driverlibrary.BoardDefaults;
import mraa.mraa;
import upm_buzzer.Buzzer;

public class GroveBuzzer extends Activity {

    private static final String TAG = "GroveBuzzer";

    upm_buzzer.Buzzer sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_buzzer);

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
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "in onDestroy() call");
        Thread.currentThread().interrupt();
        //button.delete();
    }

}


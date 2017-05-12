package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.upm.androidthings.driversupport.BoardDefaults;
import mraa.mraa;

public class VEMLUVActivity extends Activity {

    private static final String TAG = "VEMLUVActivity";

    upm_veml6070.VEML6070 uvSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vemluv);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int i2cIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Veml_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Veml_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Veml_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        uvSensor = new upm_veml6070.VEML6070(i2cIndex,0x38);
        AsyncTask.execute(uvSensorTask);
    }

    Runnable uvSensorTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            try {
                //while (true)
                for (int i =0; i < 5; i++)
                {
                    // update our values from the sensor
                    System.out.println("UV Value: "+uvSensor.getUVIntensity());
                        Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                uvSensor.delete();
                VEMLUVActivity.this.finish();

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Thread.currentThread().interrupt();
        uvSensor.delete();
    }
}

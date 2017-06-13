package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import mraa.mraa;

public class ThermopileActivity extends Activity {

    private static final String TAG = "IRThermopileActivity";

    upm_tmp006.TMP006 thermopile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermopile);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int i2cIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Tmp_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Tmp_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Tmp_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        thermopile = new upm_tmp006.TMP006(i2cIndex, (short) 0, 64);
        AsyncTask.execute(thermopileTask);
    }

    Runnable thermopileTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            thermopile.setActive();

            try {
                while (true)
                {
                    //thermopile.sampleData()
                    // Print out temperature value in °C
                    Log.i(TAG,"Temperature: "+ thermopile.getTemperature(1) + " °C");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                thermopile.delete();
                ThermopileActivity.this.finish();

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Thread.currentThread().interrupt();
        thermopile.delete();
    }
}
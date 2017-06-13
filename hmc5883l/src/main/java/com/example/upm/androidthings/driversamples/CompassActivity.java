package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.upm.androidthings.driverlibrary.BoardDefaults;
import mraa.mraa;


public class CompassActivity extends Activity {

    private static final String TAG = "CompassActivity";

    upm_hmc5883l.Hmc5883l compass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int i2cIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Compass_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Compass_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Compass_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        compass = new upm_hmc5883l.Hmc5883l(i2cIndex);
        AsyncTask.execute(compassTask);

    }
    Runnable compassTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            short[] pos;

            compass.set_declination(0.2749f);

            try {
                while(true){
                    compass.update(); // Update the coordinates

                    pos = compass.coordinates();
                    Log.i(TAG, "coordinates: %5d %5d %5d " + pos[0] + pos[1] + pos[2]);
                    Log.i(TAG, "heading: %5.2f direction: %3.2f\n" +
                            compass.heading() + compass.direction());
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {

                compass.delete();
                CompassActivity.this.finish();

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Thread.currentThread().interrupt();
        compass.delete();
    }
}

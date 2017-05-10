package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.upm.androidthings.driversupport.BoardDefaults;
import mraa.mraa;

public class BMETempActivity extends Activity {

    private static final String TAG = "BMETempActivity";

    upm_bmp280.BME280 tphSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmetemp);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int i2cIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Bme_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Bme_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Bme_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        tphSensor = new upm_bmp280.BME280(i2cIndex);
        AsyncTask.execute(tphSensorTask);
    }

    Runnable tphSensorTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            try {
                while (true)
                {
                    // update our values from the sensor
                    tphSensor.update();

                    Log.i(TAG,"Compensation Temperature: " + tphSensor.getTemperature()
                            + " C / "
                            + tphSensor.getTemperature(true)
                            + " F");

                    Log.i(TAG,"Pressure: "
                            + tphSensor.getPressure()
                            + " Pa");

                    Log.i(TAG,"Computed Altitude: "
                            + tphSensor.getAltitude()
                            + " m");

                    Log.i(TAG,"Humidity: "
                            + tphSensor.getHumidity()
                            + " %RH");
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                tphSensor.reset();
                BMETempActivity.this.finish();

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Thread.currentThread().interrupt();
        tphSensor.reset();
    }
}

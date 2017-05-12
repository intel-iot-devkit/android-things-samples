package com.apray.plugin;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import mraa.mraa;



public class ATmraa extends CordovaPlugin {
    private static final String TAG = "ATmraa";

    private CallbackContext callbackContext;
    upm_bmp280.BME280 tphSensor;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        int i2cIndex = 0;
        tphSensor = new upm_bmp280.BME280(i2cIndex);

    }


    @Override
    public boolean execute(String action, JSONArray data, CallbackContext _callbackContext) throws JSONException {
        callbackContext =_callbackContext;

        if (action.equals("greet")) {

            String name = data.getString(0);
            String message = "Hello, " + name;
            callbackContext.success(message);

            return true;

        }else if (action.equals("NoNo")) {
            AsyncTask.execute(tphSensorTask);
            return true;
        } else {
            
            return false;

        }
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
            }
        }
    };

}

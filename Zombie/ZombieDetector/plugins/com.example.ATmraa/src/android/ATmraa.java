package com.apray.plugin;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import mraa.mraa;

import static java.lang.Thread.sleep;


public class ATmraa extends CordovaPlugin {
    private static final String TAG = "ATmraa";

    private CallbackContext callbackContext;
    private CallbackContext RotaryEncodercallbackContext;
    private CallbackContext TMP006callbackContext;

    upm_bmp280.BME280 tphSensor;
    upm_tmp006.TMP006 thermopile;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        int i2cIndex = 0;
        tphSensor = new upm_bmp280.BME280(i2cIndex);
        thermopile = new upm_tmp006.TMP006(i2cIndex, (short) 0, 64);

    }


    @Override
    public boolean execute(String action, JSONArray data, CallbackContext _callbackContext) throws JSONException {


        if (action.equals("greet")) {
            callbackContext =_callbackContext;
            String name = data.getString(0);
            String message = "Hello, " + name;
            callbackContext.success(message);

            return true;

        }else if (action.equals("BME280")) {
            callbackContext =_callbackContext;
            tphSensor.update();
            String RequestValue = data.getString(0);
            if(RequestValue.contains("Temperature")){
                callbackContext.success(tphSensor.getTemperature() + " C");
            }else if(RequestValue.contains("Pressure")){
                callbackContext.success(tphSensor.getPressure() + " Pa");
            }else if(RequestValue.contains("Altitude")){
                callbackContext.success(tphSensor.getAltitude() + " m");
            }else if(RequestValue.contains("Humidity")){
                callbackContext.success(tphSensor.getHumidity() + " %RH");
            }else {
                callbackContext.error("Unknow Mode "+RequestValue);
                return false;
            }
            //tphSensor.reset();
            return true;
        }else if (action.equals("TMP006")) {
            TMP006callbackContext =_callbackContext;
            AsyncTask.execute(thermopileTask);
            return true;
        }else if (action.equals("RotaryEncoder")) {
            RotaryEncodercallbackContext =_callbackContext;
            AsyncTask.execute(RotaryEncoderTask);
            return true;
        }  else {

            return false;

        }
    }

    Runnable thermopileTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            //android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            thermopile.resetSensor();
            thermopile.setActive();
            float temp = thermopile.getTemperature(1);

            Log.i(TAG,"Temperature: "+ temp + " °C");

            while (temp == -273.2f){
                temp = thermopile.getTemperature(1);
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Log.i(TAG,"Temperature: "+ temp + " °C");
            TMP006callbackContext.success("Temperature: "+ temp + " °C");
            thermopile.setStandby();
        }
    };

    Runnable RotaryEncoderTask = new Runnable() {
        int pos = 0;
        @Override
        public void run() {
            // Moves the current thread into the background
            //android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            RotaryEncodercallbackContext.success(pos);
        }
    };

}

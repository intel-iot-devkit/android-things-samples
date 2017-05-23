
package com.apray.plugin;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mraa.mraa;
import upm_bmp280.BME280;
import upm_buzzer.javaupm_buzzerConstants;
import upm_tmp006.TMP006;
import upm_grove.GroveLed;
import upm_buzzer.Buzzer;

import static java.lang.Thread.sleep;


public class ATmraa extends CordovaPlugin {
    private static final String TAG = "ATmraa";

    private CallbackContext BME280callbackContext;
    private CallbackContext TMP006callbackContext;

    private int TMP006i2cIndex =0;
    private int BME280i2cIndex = 0;
    String BME280RequestValue;
    private boolean LEDSatus = false;
    private CallbackContext LEDcallbackContext;
    private long LEDdelay = 0;
    private String LEDpin = "J6_47";
    private CallbackContext BUZZERcallbackContext;
    private String BUZZpin;
    private int BUZZdelay;
    private String BUZZnote;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext _callbackContext) throws JSONException {
        String i2cAddress;
        JSONObject reader = new JSONObject(data.getString(0));
        if( reader.has("i2cAddress")) i2cAddress = reader.getString("i2cAddress"); else i2cAddress = "I2C0";
        int i2cIndex = mraa.getI2cLookup(i2cAddress);
        Log.d(TAG, "i2cAddress:"+i2cAddress+ " = i2cIndex:"+i2cIndex);


        if (action.equals("BME280")) {
            BME280callbackContext =_callbackContext;
            if( reader.has("RequestSensor")) BME280RequestValue = reader.getString("RequestSensor"); else BME280RequestValue = "Temperature";
            BME280i2cIndex = i2cIndex;
            cordova.getThreadPool().execute(BME280Task);
            return true;
        }else if (action.equals("LED")) {
            LEDcallbackContext =_callbackContext;
            if( reader.has("pin")) LEDpin= reader.getString("pin"); else LEDpin = "J6_47";
            if( reader.has("on")) LEDSatus = reader.getBoolean("on"); else LEDSatus = false;
            if( reader.has("delay")) LEDdelay= reader.getLong("delay"); else LEDdelay = 0;
            cordova.getThreadPool().execute(GroveLedTask);
            return true;
        }else if (action.equals("TMP006")) {
            TMP006callbackContext =_callbackContext;
            TMP006i2cIndex = i2cIndex;
            cordova.getThreadPool().execute(thermopileTask);
            return true;
        }else if (action.equals("BUZZER")) {
            BUZZERcallbackContext =_callbackContext;
            if( reader.has("pin")) BUZZpin= reader.getString("pin"); else BUZZpin = "PWM_3";
            if( reader.has("note")) BUZZnote= reader.getString("note"); else BUZZnote = "DO";
            if( reader.has("delay")) BUZZdelay= reader.getInt("delay"); else BUZZdelay = 500000;


            cordova.getThreadPool().execute(GroveBuzzerTask);
            return true;
        }  else {
            return false;
        }
    }

    Runnable GroveBuzzerTask = new Runnable() {

        @Override
        public void run() {
            int pinIndex = mraa.getPwmLookup(BUZZpin);
            Buzzer buzz = new Buzzer(pinIndex);
            Log.d(TAG, "Pwm:"+LEDpin+" = pinIndex:" + pinIndex);
            int note;
            switch (BUZZnote) {
                case "DO":
                    note = javaupm_buzzerConstants.BUZZER_DO;
                    break;
                case "RE":
                    note = javaupm_buzzerConstants.BUZZER_RE;
                    break;
                case "MI":
                    note = javaupm_buzzerConstants.BUZZER_MI;
                    break;
                case "FA":
                    note = javaupm_buzzerConstants.BUZZER_FA;
                    break;
                case "SOL":
                    note = javaupm_buzzerConstants.BUZZER_SOL;
                    break;
                case "LA":
                    note = javaupm_buzzerConstants.BUZZER_LA;
                    break;
                case "SI":
                    note = javaupm_buzzerConstants.BUZZER_SI;
                    break;
                default:
                    note = javaupm_buzzerConstants.BUZZER_DO;
            }
            buzz.playSound(note,BUZZdelay);
            buzz.delete();
            BUZZERcallbackContext.success("ok");

        }
    };



    Runnable GroveLedTask = new Runnable() {

        @Override
        public void run() {
            int pinIndex = mraa.getGpioLookup(LEDpin);
            GroveLed led = new GroveLed(pinIndex);
            Log.d(TAG, "GPIO:"+LEDpin+" = pinIndex:" + pinIndex);
            if (LEDSatus) {
                led.on();
                try {
                    sleep(LEDdelay/2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                led.delete();
                LEDcallbackContext.success("On");
            } else{
                led.off();
                try {
                    sleep(LEDdelay/2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                led.delete();
                LEDcallbackContext.success("Off");
            }


        }
    };

    Runnable BME280Task = new Runnable() {

        @Override
        public void run() {
            BME280 tphSensor = new BME280(BME280i2cIndex);
            tphSensor.update();

            if(BME280RequestValue.contains("Temperature")){
                BME280callbackContext.success(""+tphSensor.getTemperature());
            }else if(BME280RequestValue.contains("Pressure")){
                BME280callbackContext.success(""+tphSensor.getPressure());
            }else if(BME280RequestValue.contains("Altitude")){
                BME280callbackContext.success(""+tphSensor.getAltitude());
            }else if(BME280RequestValue.contains("Humidity")){
                BME280callbackContext.success(""+tphSensor.getHumidity());
            }else {
                BME280callbackContext.error("Unknow Mode "+BME280RequestValue);
            }
            tphSensor.delete();
        }
    };

    Runnable thermopileTask = new Runnable() {

        @Override
        public void run() {

            TMP006 thermopile = new TMP006(TMP006i2cIndex, (short) 0, 64);
            thermopile.setActive();
            float temp = thermopile.getTemperature(1);
            Log.d(TAG,"getTemperature: "+temp);
            while (temp == -273.2f){
                temp = thermopile.getTemperature(1);
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    BME280callbackContext.error("InterruptedException");
                }
            }
            TMP006callbackContext.success(""+ temp);
            Log.d(TAG,"gotTemperature: "+temp);
            thermopile.resetSensor();
            thermopile.delete();
        }
    };


}

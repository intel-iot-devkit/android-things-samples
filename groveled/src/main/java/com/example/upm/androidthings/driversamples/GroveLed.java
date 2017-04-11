package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;

import mraa.mraa;

public class GroveLed extends Activity {
    private static final String TAG = "GroveLED";
    static {
        try {
            System.loadLibrary("javaupm_grove");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native code library failed to load.\n" +  e);
            System.exit(1);
        }
    }

    private BoardDefaults bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_led);

        bd = new BoardDefaults(this.getApplicationContext());

        int gpioIndex = -1;
        System.out.println("in App's onCreate");

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.LED_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.LED_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.LED_Joule_Tuchuck));
                System.out.println("gpioIndex"+gpioIndex);
                break;
            default:
                throw new IllegalStateException("Unknown Build.DEVICE ");
        }

        try {

            System.out.println("pinId" + gpioIndex);
            upm_grove.GroveLed led = new upm_grove.GroveLed(gpioIndex);

            for (int i = 0; i < 10; ++i) {
                led.on();
                Thread.sleep(1000);
                led.off();
                Thread.sleep(1000);
            }
            led.delete();


        }catch (Exception e) {
            Log.e(TAG, "Error in UPM APIs", e);
        }

    }
}

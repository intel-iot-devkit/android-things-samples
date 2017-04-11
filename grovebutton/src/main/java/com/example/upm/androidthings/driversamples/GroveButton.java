package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;

import mraa.mraa;

public class GroveButton extends Activity {
    private static final String TAG = "GroveButtonActivity";
    static {
        try {
            System.loadLibrary("javaupm_grove");
        } catch (UnsatisfiedLinkError e) {
            System.err.println(
                    "Native code library failed to load.\n" +  e);
            System.exit(1);
        }
    }

    private BoardDefaults bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_button);

        bd = new BoardDefaults(this.getApplicationContext());

        int gpioIndex = -1;
        System.out.println("in App's onCreate");

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.BUTTON_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.BUTTON_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.BUTTON_Joule_Tuchuck));
                System.out.println("gpioIndex"+gpioIndex);
                break;
            default:
                throw new IllegalStateException("Unknown Build.DEVICE ");
        }

        upm_grove.GroveButton button = new upm_grove.GroveButton(gpioIndex);
        try {
            while (true) {
                System.out.println(button.name() + " value is " + button.value());
                Thread.sleep(1000);
            }
        }
        catch(Exception e) {
            Log.e(TAG, "Error in UPM API", e);
        }
    }
}

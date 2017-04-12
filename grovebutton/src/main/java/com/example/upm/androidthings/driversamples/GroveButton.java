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
            Log.e(TAG, "Native code library failed to load." +  e);
            System.exit(1);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_button);

        int gpioIndex = -1;
        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.BUTTON_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.BUTTON_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.BUTTON_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        try {
            upm_grove.GroveButton button = new upm_grove.GroveButton(gpioIndex);

            // This needs to be moved to a separate thread.
            while (true) {
                Log.i(TAG, button.name() + " value is " + button.value());
                Thread.sleep(1000);
            }

        // You shouldn't catch unclassified exceptions.
        // You probably should exit should an exception be caught.
        } catch(Exception e) {
            Log.e(TAG, "Error in UPM API", e);
        }
    }
}

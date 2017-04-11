package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;

import mraa.mraa;

public class GroveTouch extends Activity {

    private static final String TAG = "GroveTouchActivity";
    static {
        try {
            System.loadLibrary("javaupm_ttp223");
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
        setContentView(R.layout.activity_grove_touch);

        bd = new BoardDefaults(this.getApplicationContext());

        int gpioIndex = -1;
        System.out.println("in App's onCreate");

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.TOUCH_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.TOUCH_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.TOUCH_Joule_Tuchuck));
                System.out.println("gpioIndex"+gpioIndex);
                break;
            default:
                throw new IllegalStateException("Unknown Build.DEVICE ");
        }

        try {
            upm_ttp223.TTP223 touch = new upm_ttp223.TTP223(gpioIndex);

            while (true) {
                if (touch.isPressed())
                    System.out.println(touch.name() + " is pressed");
                else
                    System.out.println(touch.name() + " is not pressed");

                Thread.sleep(1000);
            }

        }catch (Exception e) {
            Log.e(TAG, "Error in UPM APIs", e);
        }
    }
}

package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;

import mraa.mraa;
public class GroveRelay extends Activity {

    private static final String TAG = "GroveRelayActivity";

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
        setContentView(R.layout.activity_grove_relay);

        bd = new BoardDefaults(this.getApplicationContext());

        int gpioIndex = -1;
        System.out.println("in App's onCreate");

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.RELAY_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.RELAY_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.RELAY_Joule_Tuchuck));
                System.out.println("gpioIndex"+gpioIndex);
                break;
            default:
                throw new IllegalStateException("Unknown Build.DEVICE ");
        }

        try {
            upm_grove.GroveRelay relay = new upm_grove.GroveRelay(gpioIndex);

            for (int i = 0; i < 6; i++) {
                relay.on();
                if (relay.isOn())
                    System.out.println("Relay is on");
                Thread.sleep(1000);

                relay.off();
                if (relay.isOff())
                    System.out.println("Relay is off");
                Thread.sleep(1000);
            }

        }catch (Exception e) {
            Log.e(TAG, "Error in UPM APIs", e);
        }
    }
}

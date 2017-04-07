package com.intel.example.groveLED;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.res.Resources;
import mraa.*;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "GroveLED";
    private Resources res;


    static {
        try {
            System.loadLibrary("javaupm_grove");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int gpioIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.LED_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.LED_Edison_Arduino));
                Log.e(TAG, "Edison Arudino IO pin: " + getString(R.string.LED_Edison_Arduino) + ". gpioIndex: " + gpioIndex);
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.LED_Joule));
                break;
            case BoardDefaults.DEVICE_NOT_KNOWN:
                Log.e(TAG, "Unknown device");
                System.exit(1);
                break;
        }

        // This needs to be moved to a background thread, it isn't proper to sleep/hang in the main thread.
        try {
            upm_grove.GroveLed led = new upm_grove.GroveLed(gpioIndex);

            for (int i = 0; i < 10; ++i) {
                led.on();
                Thread.sleep(1000);
                led.off();
                Thread.sleep(1000);
            }
            led.delete();
        } catch (Exception e) {
            Log.e(TAG, "Error in UPM APIs", e);
        }
    }
}
package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import mraa.mraa;
public class GroveRelay extends Activity {

    private static final String TAG = "GroveRelayActivity";

    static {
        try {
            System.loadLibrary("javaupm_grove");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native code library failed to load." +  e);
            System.exit(1);
        }
    }

    upm_grove.GroveRelay relay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_relay);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int gpioIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

            relay = new upm_grove.GroveRelay(gpioIndex);
            relayTask.run();

    }

    Runnable relayTask = new Runnable() {

        @Override
        public void run() {
            //Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            try {
                for (int i = 0; i < 6; i++) {
                    relay.on();
                    if (relay.isOn())
                        Log.i(TAG, "Relay is on");
                    Thread.sleep(1000);

                    relay.off();
                    if (relay.isOff())
                        Log.i(TAG, "Relay is off");
                    Thread.sleep(1000);
                }
                relay.off();
                relay.delete();

            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}

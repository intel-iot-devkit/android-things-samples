package com.example.upm.androidthings.driversamples;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import mraa.mraa;
import mraa.Gpio;
import mraa.Dir;
import mraa.Platform;
/**
 * Created by brillo on 4/11/17.
 */

public class BoardDefaults {
    // returned by Build.device (does not differentiate the carrier boards)
    private static final String DEVICE_EDISON = "edison";
    private static final String DEVICE_JOULE = "joule";

    // determined by this module (includes the carrier board information)
    // note: edison_sparkfun and edison_miniboard use the same busses and gpios
    //       so we don't distinguish between them.
    static final String DEVICE_EDISON_ARDUINO = "edison_arduino";
    static final String DEVICE_EDISON_SPARKFUN = "edison_sparkfun";
    static final String DEVICE_JOULE_TUCHUCK = "joule_tuchuck";
    static final String DEVICE_NOT_KNOWN = "UNKNOWN";
    private final Context context;
    private String sBoardVariant = "";
    private static final String TAG = "BoardDefaults";

    public String GPIO_Pin;

    Resources res;

    public BoardDefaults(Context applicationContext) {
        this.context = applicationContext;
    }


    public String getBoardVariant() {
        if (!sBoardVariant.isEmpty()) {
            return sBoardVariant;
        }
        res = this.context.getResources();
        sBoardVariant = Build.DEVICE;
        // For the edison check the pin prefix
        // to always return Edison Breakout pin name when applicable.

        if (sBoardVariant.equals(DEVICE_EDISON)) {
            System.out.println("specification value is " + mraa.class.getPackage().getSpecificationVersion());
            if (mraa.getGpioLookup(res.getString(R.string.GPIO_Edison_Arduino)) != -1)
                sBoardVariant = DEVICE_EDISON_ARDUINO;
            else
                sBoardVariant = DEVICE_EDISON_SPARKFUN;
        } else if(sBoardVariant.equals(DEVICE_JOULE)) {
            sBoardVariant = DEVICE_JOULE_TUCHUCK;
        } else{
            sBoardVariant = DEVICE_NOT_KNOWN;
        }
        return sBoardVariant;
    }
}

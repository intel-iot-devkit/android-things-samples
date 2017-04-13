package com.example.upm.androidthings.driversamples;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import mraa.mraa;

public class BoardDefaults {
    // returned by Build.device (does not differentiate the carrier boards)
    static final String DEVICE_EDISON = "edison";
    static final String DEVICE_JOULE = "joule";

    // determined by this module (includes the carrier board information)
    // note: edison_sparkfun and edison_miniboard use the same busses and gpios
    //       so we don't distinguish between them.
    static final String DEVICE_EDISON_ARDUINO = "edison_arduino";
    static final String DEVICE_EDISON_SPARKFUN = "edison_sparkfun";
    static final String DEVICE_JOULE_TUCHUCK = "joule_tuchuck";
    static final String DEVICE_NOT_KNOWN = "UNKNOWN";

    private Context context;
    private Resources res;
    private String sBoardVariant = "";

    public BoardDefaults(Context applicationContext) {
        this.context = applicationContext;
        res = this.context.getResources();
    }

    public String getBoardVariant() {
        if (!sBoardVariant.isEmpty()) {
            return sBoardVariant;
        }

        // We start with the most generic device description and try to narrow it down.
        sBoardVariant = Build.DEVICE;

        if (sBoardVariant.equals(DEVICE_EDISON)) {
            // For the edison check the pin prefix
            // to always return Edison Breakout pin name when applicable.
            if (mraa.getGpioLookup(res.getString(R.string.GPIO_Edison_Arduino)) != -1)
                sBoardVariant = DEVICE_EDISON_ARDUINO;
            else
                sBoardVariant = DEVICE_EDISON_SPARKFUN;

        } else if (sBoardVariant.equals(DEVICE_JOULE)) {
            sBoardVariant = DEVICE_JOULE_TUCHUCK;

        } else {
            sBoardVariant = DEVICE_NOT_KNOWN;
        }

        return sBoardVariant;
    }
}

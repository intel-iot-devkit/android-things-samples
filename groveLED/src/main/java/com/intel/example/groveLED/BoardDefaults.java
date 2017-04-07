package com.intel.example.groveLED;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import mraa.*;


public class BoardDefaults {
    // private static final String TAG = "BoardDefaults";

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
    Context context;
    static Resources res;


    String sBoardVariant = "";

    public BoardDefaults(Context applicationContext) {
        this.context = applicationContext;
        res = this.context.getResources();
    }

    public String getBoardVariant() {
        if (!sBoardVariant.isEmpty()) {
            return sBoardVariant;
        }

        // Build knows about the module... we have to determine the carrier board as well.
        sBoardVariant = Build.DEVICE;

        if (sBoardVariant.equals(DEVICE_EDISON)) {
            // Arduino board (only) has the port expander for this GPIO ping.
            // If this fails, we must be on a miniboard or a sparkfun.
            if (-1 != mraa.getGpioLookup(res.getString(R.string.GPIO_Edison_Arduino)))
                sBoardVariant = DEVICE_EDISON_ARDUINO;
            else
                sBoardVariant = DEVICE_EDISON_SPARKFUN;
        } else if (sBoardVariant.equals(DEVICE_JOULE)) {
            sBoardVariant = DEVICE_JOULE_TUCHUCK;
        } else
            sBoardVariant = DEVICE_NOT_KNOWN;

        return sBoardVariant;
    }
}
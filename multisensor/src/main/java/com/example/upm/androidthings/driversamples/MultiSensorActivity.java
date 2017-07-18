package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.upm.androidthings.driversupport.BoardDefaults;

import mraa.mraa;

public class MultiSensorActivity extends Activity {

    private static final String TAG = "MultiSensorActivity";

    TextView tv;
    ImageView im1;
    ImageView im2;
    ImageView im3;

    private boolean RedledStatus;
    private boolean BlueledStatus;
    private boolean GreenledStatus;

    upm_tmp006.TMP006 thermopile;
    upm_jhd1313m1.Jhd1313m1 lcd;
    upm_grove.GroveLed Redled;
    upm_grove.GroveButton button;
    upm_grove.GroveLed Blueled;
    upm_ttp223.TTP223 touch;
    upm_grove.GroveLed Greenled;
    private boolean Status;

    float Temp = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_sensor);


        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int i2cIndex = -1;
        int i2cLcdIndex = -1;
        int gpioRedLedIndex = -1;
        int gpioButtonIndex = -1;
        int gpioBlueLedIndex = -1;
        int gpioTouchIndex = -1;
        int gpioGreenLedIndex = -1;
        tv = (TextView)findViewById(R.id.activity_Temp_Values);
        im1 = (ImageView)findViewById(R.id.imageView);
        im2 = (ImageView)findViewById(R.id.imageView2);
        im3 = (ImageView)findViewById(R.id.imageView3);

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Tmp_Edison_Arduino));
                i2cLcdIndex = mraa.getI2cLookup(getString(R.string.Lcd_Edison_Arduino));
                gpioRedLedIndex = mraa.getGpioLookup(getString(R.string.LedRed_Edison_Arduino));
                gpioButtonIndex = mraa.getGpioLookup(getString(R.string.Button_Edison_Arduino));
                gpioBlueLedIndex = mraa.getGpioLookup(getString(R.string.LedBlue_Edison_Arduino));
                gpioTouchIndex = mraa.getGpioLookup(getString(R.string.Touch_Edison_Arduino));
                gpioGreenLedIndex = mraa.getGpioLookup(getString(R.string.LedGreen_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Tmp_Edison_Sparkfun));
                i2cLcdIndex = mraa.getI2cLookup(getString(R.string.Lcd_Edison_Sparkfun));
                gpioRedLedIndex = mraa.getGpioLookup(getString(R.string.LedRed_Edison_Sparkfun));
                gpioButtonIndex = mraa.getGpioLookup(getString(R.string.Button_Edison_Sparkfun));
                gpioBlueLedIndex = mraa.getGpioLookup(getString(R.string.LedBlue_Edison_Arduino));
                gpioTouchIndex = mraa.getGpioLookup(getString(R.string.Touch_Edison_Sparkfun));
                gpioGreenLedIndex = mraa.getGpioLookup(getString(R.string.LedGreen_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                i2cIndex = mraa.getI2cLookup(getString(R.string.Tmp_Joule_Tuchuck));
                i2cLcdIndex = mraa.getI2cLookup(getString(R.string.Lcd_Joule_Tuchuck));
                gpioRedLedIndex = mraa.getGpioLookup(getString(R.string.LedRed_Joule_Tuchuck));
                gpioButtonIndex = mraa.getGpioLookup(getString(R.string.Button_Joule_Tuchuck));
                gpioBlueLedIndex = mraa.getGpioLookup(getString(R.string.LedBlue_Joule_Tuchuck));
                gpioTouchIndex = mraa.getGpioLookup(getString(R.string.Touch_Joule_Tuchuck));
                gpioGreenLedIndex = mraa.getGpioLookup(getString(R.string.LedGreen_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        thermopile = new upm_tmp006.TMP006(i2cIndex, (short) 0, 64);
        //lcd = new upm_jhd1313m1.Jhd1313m1(i2cLcdIndex);
        Redled = new upm_grove.GroveLed(gpioRedLedIndex);
        button = new upm_grove.GroveButton(gpioButtonIndex);
        Blueled = new upm_grove.GroveLed(gpioBlueLedIndex);
        touch = new upm_ttp223.TTP223(gpioTouchIndex);
        Greenled = new upm_grove.GroveLed(gpioGreenLedIndex);
        AsyncTask.execute(thermopileTask);
    }

    Runnable thermopileTask = new Runnable() {

        @Override
        public void run() {
            // Moves the current thread into the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

            int ndx = 0;
            char TempInC;
            thermopile.setActive();

            try {
                /*if (lcd != null ) {
                    lcd.clear();
                }
                    short[][] rgb = new short[][]{
                            {0xd1, 0x00, 0x00},   // red
                            {0xff, 0x66, 0x22},   // orange
                            {0xff, 0xda, 0x21},   // yellow
                            {0x33, 0xdd, 0x00},   // green
                            {0x11, 0x33, 0xcc},   // blue
                            {0x22, 0x00, 0x66},   // violet
                            {0x33, 0x00, 0x44}};  // darker violet*/

                while (true)
                {
                    // Print out temperature value in °C
                    Temp = thermopile.getTemperature(1);
                    Log.i(TAG,"Temperature: "+ Temp + " °C");
                    Status = true;
                    //updateUI((int)Temp);
                    //Thread.sleep(1000);

                    // Alternate rows on the LCD
                    //lcd.setCursor(ndx % 2, 0);

                    // Change the color
                    /*short r = rgb[ndx % 7][0];
                    short g = rgb[ndx % 7][1];
                    short b = rgb[ndx % 7][2];*/
                    //lcd.setColor(r, g, b);

                    //lcd.write("Temp" +"       "+ ndx);

                    // Echo via printf
                    //Log.d(TAG, "Hello World" + ndx++);
                    // Log.d(TAG, String.format("rgb: 0x%02x%02x%02x", r, g, b));

                    updateUI();

                    Thread.sleep(1000);
                    if(Temp >= 27)
                    {
                        Log.i(TAG,"Temperature: is >= 27");
                        Redled.on();
                        RedledStatus = true;
                    }else{
                        Redled.off();
                        RedledStatus = false;
                    }
                    Thread.sleep(1000);
                    Log.i(TAG, button.name() + " value is " + button.value());
                    if (button.value() == 0)
                    {
                        Log.i(TAG,"In Button value is 0");
                        Blueled.off();
                        BlueledStatus = false;
                    }
                    else
                    {
                        Log.i(TAG,"In Button value is 1");
                        Blueled.on();
                        BlueledStatus = true;

                    }
                    if (touch.isPressed())
                    {
                        Log.i(TAG,"In Touch is pressed");
                        Greenled.on();
                        GreenledStatus = true;
                    }
                    else
                    {
                        Log.i(TAG,"In Touch is Released");
                        Greenled.off();
                        GreenledStatus = false;
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                //lcd.delete();
                thermopile.delete();
                Redled.off();
                Redled.delete();
                button.delete();
                Blueled.off();
                Blueled.delete();
                touch.delete();
                Greenled.off();
                Greenled.delete();
                MultiSensorActivity.this.finish();

            }
        }
    };

    private void updateUI() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    tv.setText("Temp values" +"    " + Float.toString(Temp));
                    Log.d(TAG, "Temp values");
                if(BlueledStatus == true){
                    im3.setImageResource(R.drawable.blue_on);
                }else{
                    im3.setImageResource(R.drawable.blue_off);
                }

                if(RedledStatus == true){
                    im2.setImageResource(R.drawable.red_on);
                }else{
                    im2.setImageResource(R.drawable.red_off);
                }

                if(GreenledStatus == true){
                    im1.setImageResource(R.drawable.green_on);
                }else{
                    im1.setImageResource(R.drawable.green_off);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Thread.currentThread().interrupt();
        //lcd.delete();
        thermopile.delete();
        Redled.delete();
        button.delete();
        Blueled.delete();
        touch.delete();
        Greenled.delete();
    }
}

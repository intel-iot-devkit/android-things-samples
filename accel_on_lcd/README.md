Combined Accelerometer and LCD display sample for Android Things using UPM
---------------------------------------------------------------------------

This example demonstrates connecting an I2C Accelerometer to Android Things using UPM.
The sensor is registered to the Android Things User Sensor manager and then the sensor manager
is used to read the sensor. Gravimetric readings for each (X,Y,Z) axis are then printed on
an I2C connected LCD display.

The Mma7660AccelerometerDriver Class used to register with the sensor manager.

build.gradle:

   ````
   dependencies {
           compile 'io.mraa.at.upm:upm_jhd1313m1:1.+'
           compile 'io.mraa.at.upm:upm_mma7660:1.+'
           compile 'io.mraa.at:mraa:1.+'
           provided 'com.google.android.things:androidthings:0.4-devpreview'
   }
   ````

See also the mma7660 and jhd1313m1 examples


Pre-Requisites:
---------------
Use of the Grove Kit (for Joule or Edison) makes this easy. See the following links for getting
a starter kit.

*  https://www.seeedstudio.com/Grove-Maker-Kit-for-Intel-Joule-p-2796.html
*  https://www.seeedstudio.com/Grove-Starter-Kit-V3-p-1855.html


You will need:

1. Android Things compatible board.
2. Grove header or Breakout board.
3. A jhd1313m1 LCD display (in the grove kit).
4. A mma7660 3 axis digital Accelerometer (available separately from Grove).

   https://www.seeedstudio.com/s/7660.html


Build and install:
------------------
On Android Studio, select the "accel_on_lcd" module in select box by the "Run" button
and then click on the "Run" button.


Changing the I2C bus
--------------------
The I2C bus to be used is specified in the strings.xml file (src/res/values directory).

````
<resources>
    <string name="app_name">Accelerometer Mma7660</string>
    <string name="Edison_Arduino">I2C6</string>
    <string name="Edison_Sparkfun">I2C1</string>
    <string name="Joule_Tuchuck">I2C0</string>
</resources>
````

The code will automatically determine the board type being run on (modify BoardDefaults.java) and select a string from this file for the I2C bus.
The above example uses I2C6 on the Edison Arduino shield and I2C0 on the Joule Tuchuck
development board. These strings are programmed into the Peripheral Manager and read from their
into the UPM library to determine the bus to be used.

See the top level README.md for a table describing the available I2C busses and where to find them
on the board.

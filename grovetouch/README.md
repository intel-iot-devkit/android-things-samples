Grove touch sample for Android Things using UPM
-----------------------------------------------

This example demonstrates a GPIO connection using UPM.

Pre-Requisites:
---------------
Use of the Grove Kit (for Joule or Edison) makes this easy. See the following links for getting
a starter kit.

*  https://www.seeedstudio.com/Grove-Maker-Kit-for-Intel-Joule-p-2796.html
*  https://www.seeedstudio.com/Grove-Starter-Kit-V3-p-1855.html


You will need:

1. Android Things compatible board.
2. Grove header or Breakout board.
3. A Grove touch sensor.


Build and install:
------------------

On Android Studio, select the "grovetouch" module in select box by the "Run" button
and then click on the "Run" button.


Changing the GPIO pin
---------------------
This example uses a GPIO (digital input) to read the state of a touch sensor. The GPIO could be
connected via the shield to a Grove touch sensor... or it could be a sensor
on a breakout board directly connected between a GPIO line and Vcc.

The GPIO line to be used is specified in the strings.xml file (src/res/values directory).

````
<resources>
    <string name="app_name">GroveTouch</string>

    <string name="Touch_Edison_Arduino">IO0</string>
    <string name="Touch_Edison_Sparkfun">GP20</string>
    <string name="Touch_Joule_Tuchuck">DISPLAY_0_RST_N</string>
</resources>
````

The code will automatically determine the board type being run on (modify BoardDefaults.java
in the driver library to add another board) and select a string from this file for the GPIO line.
The above example uses IO0 on the Edison Arduino shield and DISPLAY_0_RST_N on the Joule Tuchuck
development board. These strings are programmed into the Peripheral Manager and read from their
into the UPM library to determine the GPIO pin to be used.

See the top level README.md for a table describing the available GPIO pins and where to find them
on the board.

SSD1351 SPI connected display sample for Android Things using UPM
------------------------------------------------------------------

This example demonstrates connecting an SPI display to Android Things using UPM.
Commands to display text,shapes and colors are sent to the display.

Pre-Requisites:
---------------

You will need:

1. Android Things compatible board.
2. A SSD1351 display.


Build and install:
------------------

On Android Studio, select the "SSD1351" module in select box by the "Run" button
and then click on the "Run" button.

Changing the GPIO bus
--------------------

This application currently supports SPI0 as the UPM is defaulted to SPI0. 
This application requires DC and RESET which are GPIO pins. The GPIO bus to be used is specified in the strings.xml file (src/res/values directory).

````
<resources>
    <string name="app_name">SSD1351</string>

    <string name="Display_OC_Edison_Arduino">IO0</string>
    <string name="Display_DC_Edison_Arduino">IO1</string>
    <string name="Display_RESET_Edison_Arduino">IO2</string>

    <string name="Display_OC_Edison_Sparkfun">GP20</string>
    <string name="Display_DC_Edison_Sparkfun">GP19</string>
    <string name="Display_RESET_Edison_Sparkfun">GP14</string>

    <string name="Display_OC_Joule_Tuchuck">J7_64</string>
    <string name="Display_DC_Joule_Tuchuck">J6_47</string>
    <string name="Display_RESET_Joule_Tuchuck">J6_49</string>
</resources>
````

The code will automatically determine the board type being run on (modify BoarDefaults.java
in the driver library to add another board) and select a string from this file for the GPIO bus.
The above example uses IO1 and IO2 on the Edison Arduino shield and J6_47 and J6_49 on the Joule Tuchuck
development board. These strings are programmed into the Peripheral Manager and read from there
into the UPM library to determine the bus to be used.
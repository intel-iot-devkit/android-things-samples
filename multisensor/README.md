Sample Android Things Application with multiple sensors using UPM
-----------------------------------------------------------------

This example demonstrates an application with multiple sensors using I2c and GPIO connection using 
UPM.

Pre-Requisites:
---------------
Use of the Grove Kit (for Joule or Edison) makes this easy. See the following links for getting
a starter kit.

*  https://www.seeedstudio.com/Grove-Maker-Kit-for-Intel-Joule-p-2796.html
*  https://www.seeedstudio.com/Grove-Starter-Kit-V3-p-1855.html


You will need:

1. Android Things compatible board.
2. Grove header or Breakout board.
3. Joule Shield.
4. A Grove touch sensor.
5. A Grove push button.
6. A JHD1313m1 LCD display (it's in the grove kit).
7. A TMP006 Temperature Sensor.
8. 3 LEDs. One of each color. Red LED, Blue LED and Green LED.


Build and install:
------------------

On Android Studio, select the "multisensor" module in select box by the "Run" button
and then click on the "Run" button.


Changing the GPIO and I2C bus
-----------------------------
This example uses a GPIO (digital input) to read the state of a touch sensor, a GPIO (digital input)
 to read the state of a button ,3 GPIO (digital output) to light LEDs (Red, Green and Blue).
An I2C temperature sensor to get the ambient temperature and an I2c based actuator 
(jhd1313m1 LCD display) to display the temperature values.connected via the shield to the sensors.

The I2C and GPIO line to be used is specified in the strings.xml file (src/res/values directory).

````
<resources>
    <string name="app_name">MultiSensor</string>

    <string name="I2C_Edison_Arduino">I2C6</string>
    <string name="I2C_Edison_Sparkfun">I2C1</string>
    <string name="I2C_Joule_Tuchuck">I2C0</string>

    <string name="LedRed_Edison_Arduino">IO13</string>
    <string name="LedRed_Edison_Sparkfun">GP12</string>
    <string name="LedRed_Joule_Tuchuck">J6_22</string>

    <string name="Button_Edison_Arduino">IO0</string>
    <string name="Button_Edison_Sparkfun">GP20</string>
    <string name="Button_Joule_Tuchuck">J7_68</string>

    <string name="LedBlue_Edison_Arduino">IO13</string>
    <string name="LedBlue_Edison_Sparkfun">GP12</string>
    <string name="LedBlue_Joule_Tuchuck">J6_1</string>

    <string name="Touch_Edison_Arduino">IO0</string>
    <string name="Touch_Edison_Sparkfun">GP20</string>
    <string name="Touch_Joule_Tuchuck">J7_64</string>

    <string name="LedGreen_Edison_Arduino">IO13</string>
    <string name="LedGreen_Edison_Sparkfun">GP12</string>
    <string name="LedGreen_Joule_Tuchuck">J7_58</string>

</resources>
````

The code will automatically determine the board type being run on (modify BoardDefaults.java
in the driver library to add another board) and select a string from this file for the GPIO line.
These strings are programmed into the Peripheral Manager and read from there
into the UPM library to determine the GPIO pin to be used.

See the top level README.md for a table describing the available GPIO pins and where to find them
on the board.

Grove Buzzer sample for Android Things using UPM
------------------------------------------------

This example demonstrates a PWM connection using UPM.

Pre-Requisites:
---------------
Use of the Grove Kit (for Joule or Edison) makes this easy. See the following links for getting
a starter kit.

*  https://www.seeedstudio.com/Grove-Maker-Kit-for-Intel-Joule-p-2796.html
*  https://www.seeedstudio.com/Grove-Starter-Kit-V3-p-1855.html


You will need:

1. Android Things compatible board.
2. Grove header or Breakout board.
3. Joule shield.
4. A Grove buzzer.


Build and install:
------------------

On Android Studio, select the "grovebuzzer" module in select box by the "Run" button
and then click on the "Run" button.


Changing the PWM pin:
---------------------


The PWM to be used is specified in the strings.xml file (src/res/values directory).

````
<resources>
    <string name="app_name">grovebuzzer</string>

    <string name="Buzzer_Edison_Arduino">PWM0</string>
    <string name="Buzzer_Edison_Sparkfun">PWM0</string>

    <string name="Buzzer_Joule_Tuchuck">PWM_0</string>
</resources>
````

The code will automatically determine the board type being run on (modify BoarDefaults.java
in the driver library to add another board) and select a string from this file for the PWM.
The above example uses PWM0 on the Edison Arduino shield and on the Joule Tuchuck development board.
These strings are programmed into the Peripheral Manager and read from there into the UPM library
to determine the bus to be used.

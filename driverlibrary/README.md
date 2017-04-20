Driver Library for Grove Examples
---------------------------------

This library contains support code for the various grove examples.
The files in this library are gathered here for convenience... they can be copied into
the examples or split into seperate libraries as desired.


BoardDefaults.java
------------------
This code determines the SOM and carrier board that is being used based on the board information
provided by the system and the characteristics of the board (Edison Arduino has a GPIO expander).

Support for additonal SOMs and carrier boards can easily be added to this file.


Mma7660AccelerometerDriver.java
-------------------------------
This code is a class that can be used to register with the Android Things sensor manager. It
provides an interface between the sensor manager and UPM (and thus the desired sensor). Sensors of
many types can be interfaced in this way with the sensor manager.
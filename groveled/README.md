GroveLed sample for Android Things using UPM:
---------------------------------------------

This example verifies the GPIO. Verifies OnboardLED. With the same sample,
we can even connect an external led and make it blink.

Pre-Requisites:
---------------

• Android Things compatible board.
• Breakthrough board.
• 1 external LED if required.

Build and install:
------------------

On Android Studio, select the “groveled” module in select box by the “Run”
button and then click on the "Run" button.

To run on the command line, from this repositorys root directory, type

./gradlew groveled:installDebug adb shell
       am start com.example.upm.androidthings.driversamples/.GroveLed

If everything is set up correctly, the on board led blinks. If any external led is
connected to the correct pinout, the external led should blink.

License Copyright (c) 2017 Intel Corporation.  Licensed to the Apache Software Foundation
(ASF) under one or more contributor license agreements. See the NOTICE file distributed
with this work for additional information regarding copyright ownership. The ASF licenses
this file to you under the Apache License, Version 2.0 (the "License"); you may not use
this file except in compliance with the License. You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed
to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
for the specific language governing permissions and limitations under the License.



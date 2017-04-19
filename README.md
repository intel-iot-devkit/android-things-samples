Java Examples for Android Things
--------------------------------

The current version of this document assumes you have the Android Things installed on an
Intel Joule or Intel Edison device and that you have Android Studio set up (and know how to use
it to some extent).

Overview
--------
This is a walk through of how to execute the Android Things code samples on Android
Studio.  These Applications use UPM and MRAA libraries and run from Android Studio. These
examples are a good starting point for developers to write android applications using UPM
and MRAA. With these templates developers can extend to multiple sensors. Each sample is
an android module that can be run independently.

A README.md file is included in the top level of each of the sample projects. See the
project's README.md file for specific requirements for that project.


Importing the code to Android Studio
------------------------------------

1. Clone the project from github (details not provided here).
2. Select _Open Project_ from android studio and select the _andriod-things-sample_ directory.


Importing UPM Android studio projects
-------------------------------------

1. The UPM Sensor library archives maintained on github and distributed via jcenter.

2. The jcenter link to refer the available UPM library archives/packages is:
   http://jcenter.bintray.com/io/mraa/at/upm/
   
3. The build.gradle at the top level of the android-things-samples project includes the
   required directive to include the upm modules needed in that project from jcenter. The
   upm project will pull in the requried mraa dependancy.
   
   For example, the following dependency line is found in jhd1313m1/build.gradle:
   
   ````
   dependencies {
       compile 'io.mraa.at.upm:upm_jhd1313m1:1.1.0'
       compile project(':driverlibrary')
       provided 'com.google.android.things:androidthings:0.3-devpreview'
   }
    ````

   This build script pulls in io.mraa.at.upm:upm_jhd1313m1:1.1.0 from jcenter and includes the 
   driverlibrary from this project as well. Driverlibrary includes some common functions as well as
   an example sensor manager plug-in for the MMA 7660 Accelerometer.


PreRequisites
-------------
1. Android Studio: Download Android Studio from:

   https://developer.android.com/studio/index.html
   
2. SDK: Download the Android Things SDK preview image for your development board from:

   https://developer.android.com/things/preview/index.html
   
3. A Development board: You will need a development board such as the Intel Edison or Intel Joule
   with the Android Things image flashed. See Google's getting started guides for information on
   flashing  your development board.
   
4. Sensors and effectors: A grove kit with any required sensors or effectors for the example.
   See the example's README.md file.

5. Sensor Libraries on JCenter will be automatically loaded. You will need internet access:

   http://jcenter.bintray.com/io/mraa/at/upm/


Build and Install
-----------------

You can run your application directly from Android Studio or can build an APK using Android Studio
and then side-load it onto your device.

1. Installing your APK from Android Studio:

    In your project - to the right select :
    Gradle -> Your Application name -> Install -> installdebug,
    This does both compiling an application and installing it to the device.
    
2. Build apk in Android studio and side-load:

    Build -> Build APK.
    This generates a .apk (application).The application path in your project is
    module/build/outputs.
      adb install <xyz.apk>
      
3. Directly run your applicatino from Android Studio:
    Select the “Run or debug configuration” to the module which needs to be run.
    Press the Run button or shift+f10.


Start an application without the launcher
-----------------------------------------

The below steps will help you form the adb command to push start your apk:

1. Install your .apk file
   adb install <filename>.apk

2. Determine the package name (you probably already know this).
   adb logcat | grep 'Running dexopt'
 ````
 10-23 18:02:30.207  2657  2683 I PackageManager.DexOptimizer: Running dexopt (dex2oat)
 on: /data/app/vmdl385467157.tmp/base.apk pkg=org.apache.mobilespec isa=x86 vmSafeMode=false
 debuggable=true target-filter=interpret-only NOTE the pkg= field: org.apache.mobilespec
 ````

3. Find the startup action and intent
   adb shell pm dump org.apache.mobilespec | head
 ````
 Resolver Table: DUMP OF SERVICE package: Activity Resolver Table:
	 Non-Data Actions: android.intent.action.MAIN:
	 35725ea org.apache.mobilespec/.MainActivity filter c5952c4
	  Action: "android.intent.action.MAIN" Category: "android.intent.category.LAUNCHER"
	  AutoVerify=false
 ````

5. The launch command for the above is:
   adb shell command am start -n org.apache.mobilespec/.MainActivity -a android.intent.category.LAUNCHER`

6. Alternatively, you can use the gradle script:
   ./gradlew <module>:installDebug adb shell am start com.example.upm.androidthings.driversamples/.<ModuleActivity>


Device connections
------------------
See the module’s strings.xml file to change the bus or gpio pins being used for your sensors,
displays and effectors.


For IO Pinouts on Edison and Joule please check the android-things wiki
-----------------------------------------------------------------------
Edison Arduino Pinout: https://developer.android.com/things/hardware/edison-arduino-io.html

Joule pinout: https://developer.android.com/things/hardware/joule-io.html


Getting Started Guides for Edison and Joule
-------------------------------------------
Intel's getting started guides for Edison and Joule for Android Things may be found from
links on this page:

  https://software.intel.com/en-us/iot/android-things


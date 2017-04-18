Java Examples for Android Things


The current version of this document assumes you have the Android Things tree setup. 
Overview
This is a walkthrough of how to execute the Android Things code samples on Android Studio. 
These Applications use UPM and MRAA libraries and run from Android Studio. These examples are a good starting point for developers to write android applications using UPM and MRAA. With these templates developers can extend to multiple sensors. Each sample is an android module that can be run independently.
Importing the code to Android Studio

1.	Create a new project in Android Studio : Write down the Application name of your choice ( example : ATExamples) and the Domain of your choice.
	
2.	Import Module to your project: 
	Once you have your project open - File -> New -> Import Module 
You will be prompted to enter the source directory :

Pull the examples with git clone command as below:
Git clone https://github.com/intel-iot-devkit/upm.git
Now, in Android studio import module, when prompted for the source directories path:
$home/upm/examples/android-things/<Sensor ModuleName(Grovebutton/jhd1313m1)>

Importing UPM Sensor Android Archives to Android studio project:
1.	The UPM Sensor library archives are promoted to Jcenter.
2.	Jcenter link to refer the available UPM library archives/packages is: http://jcenter.bintray.com/io/mraa/at/upm/
3.	To pull the archives to android studio, in the module’s build.gradle file update the dependencies with the below line.
a.	dependencies {
compile 'io.mraa.at.upm:upm_ttp223:1.1.0'
}
     	The syntax of the line in dependencies is
			Compile ‘Group ID: Artifact ID: Version’

	The build script states that the project requires upm_ttp223 touch sensor for it to compile. The default repository mentioned in android studio is Jcenter, These packages are promoted to Jcenter. so while compiling, the availability of packages are checked in Jcenter and if available saves it locally to cache and compiles.

PreRequisites:
1.	Android studio: Download from https://developer.android.com/studio/index.html
2.	Obtain Android things SDK preview image for development platform :https://developer.android.com/things/preview/index.html
3.	Development board (Edison, Joule etc..) with android things image flashed.
4.	Grove Kit•Sensors as mentioned in each module.
5.	•Sensor Libraries on JCenter: http://jcenter.bintray.com/io/mraa/at/upm/
Build and install
Creating and installing the module APK: This can be done in either of the following ways
1.	Android Studio UI  : In your project - to the right select :
		Gradle -> Your Application name -> Install -> installdebug,
This does both compiling an application and installing it to the device. OR
2.	Build apk in Android studio.
Build -> Build APK.
 This generates a .apk (application).The application path in your project is module/build/outputs.
		adb install <xyz.apk>
3. In android studio, select the “Run or debug configuration” to the module which needs to be run. Press the Run button or shift+f10.
   This step builds, installs and launches the application on the connected device.
Start an application without the launcher
The below steps will help you form the adb command to push start your apk :
1.	adb install <filename>.apk
2.	adb logcat | grep 'Running dexopt'
10-23 18:02:30.207  2657  2683 I PackageManager.DexOptimizer: Running dexopt (dex2oat) on: /data/app/vmdl385467157.tmp/base.apk pkg=org.apache.mobilespec isa=x86 vmSafeMode=false debuggable=true target-filter=interpret-only
NOTE the pkg= field: org.apache.mobilespec
3.	adb shell pm dump org.apache.mobilespec > pm.dump
4.	head pm.dump
Look for the Activity Resolver Table:
DUMP OF SERVICE package:
Activity Resolver Table:
                                  Non-Data Actions:
                                   android.intent.action.MAIN:	
                                     35725ea org.apache.mobilespec/.MainActivity filter c5952c4
                                       Action: "android.intent.action.MAIN"
                                       Category: "android.intent.category.LAUNCHER"
                                       AutoVerify=false 
5. The launch command is:
adb shell command am start -n org.apache.mobilespec/.MainActivity -a android.intent.category.LAUNCHER`

The other way is
./gradlew <module>:installDebug
adb shell am start com.example.upm.androidthings.driversamples/.<ModuleActivity>

Device connections
•	In each module’s strings.xml file, there is a possibility of changing the pin names and decide on the pins the sensors has to be connected.

For IO Pinouts on Edison and Joule please check the android-things wiki:

Edison Arduino Pinout: https://developer.android.com/things/hardware/edison-arduino-io.html
Joule pinout: https://developer.android.com/things/hardware/joule-io.html

This folder contains Cordova plugins for Android Things, there are three types of status:

* Existing plugins w/o any changes, just follow normal [add plugins](http://cordova.apache.org/docs/en/latest/guide/cli/index.html#add-plugins) steps

* Existing plugins need patch (not yet upstreamed), need get the git repo URL with patch first. 
```
cd <your_sample_dir>
cordova plugin add <repo_with_patch>

```
Note, those patches will be upstreamed, keep them in downstream repo is just because of timing.

* New plugins, steps below
```
cd $HOME
git clone https://github.com/intel-iot-devkit/android-things-samples
cd <your_sample_dir>
cordova plugin add $HOME/android-things-samples/Cordova/<the_plugin_dir>

```

## Plugin Status
| Plugin | Type | Status | Comment |
|--------|------|--------|---------|
| [Battery Status](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-battery-status/index.html)           | existing w/o patch | Verified on AC | No battery on Joule |
| [Console](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-console/index.html)                         | existing w/o patch | Verified |  |
| [Device](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-device/index.html)                           | existing w/o patch | Verified |  |
| [Dialog](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-dialogs/index.html)                          | existing w/o patch | Verified |  |
| [InAppBrowser](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-inappbrowser/index.html)               | existing w/o patch | Verified |  |
| [Splashscreen](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-splashscreen/index.html)               | existing w/o patch | Verified |  |
| [File](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-file/index.html)                               | existing w/o patch | Verified |  |
| [Globalization](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-globalization/index.html)             | existing w/o patch | Verified |  |
| [Whitelist](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-whitelist/index.html)                     | existing w/o patch | Verified |  |
| [Network Infromation](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-network-information/index.html) | existing w/o patch | Verified |  |
| [File Transfer](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-file-transfer/index.html)             | existing w/o patch | Verified |  |
| [Vibration](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-vibration/index.html)                     | existing w/ patch  | Verified on [repo](https://github.com/fujunwei/cordova-plugin-vibration/tree/buzzer) |  |
| [Device Motion](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-device-motion/index.html)             | existing w/ patch  | WIP on [repo](https://github.com/fujunwei/cordova-plugin-device-motion/tree/android_things) |  |
| [Device Orientation](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-device-orientation/index.html)   | existing w/ patch  | WIP |  |
| [Geolocation](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-geolocation/index.html)                 | existing w/ patch  | WIP |  |
| [Media](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-media/index.html)                             | existing w/ patch?  | WIP | SW codec only  |
| [Camera](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-camera/index.html)                           | existing w/ patch? | Wait for native support |  |
| [Media Capture](http://cordova.apache.org/docs/en/latest/reference/cordova-plugin-media-capture/index.html)             | existing w/ patch? | Wait for native support |  |
| LED | New plugin | WIP  |  |

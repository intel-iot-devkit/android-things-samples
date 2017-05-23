# Cordova Intel Joule Camara Plugin

Simple plugin that returns jpge form the Intel Joule Camara.

## Using

```js

    var PhotoSuccess = function(imageData) {
        document.getElementById('myImage').src =  "data:image/jpeg;base64," + imageData;
    }

    var failure = function(message) {
        if(message == "IOException"){
            console.log("Restart the TN_Shell");
        }else{
            console.log("Error calling ATCamara Plugin");
        }
    }

   	ATCamara.TakePhoto({Name:"out.bmp",Width:400,Height:400,Contrast:1,Brightness:1},PhotoSuccess,failure);
```

### TakePhoto
Name: Location In /sdcard/Pictures/			
	defalut: "out.bmp"

Width: Resize the retured image to this Width.
	defalut: 400

Height: Resize the retured image to this Height.
	defalut: 400

Contrast: sets the image Contrast.
	defalut: 90

Brightness: sets the image Brightness.
	defalut: 10

## More Info

For more information on setting up Cordova see [the documentation](http://cordova.apache.org/docs/en/latest/guide/cli/index.html)

For more info on plugins see the [Plugin Development Guide](http://cordova.apache.org/docs/en/latest/guide/hybrid/plugins/index.html)

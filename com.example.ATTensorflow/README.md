# Cordova Tensorflow Plugin

Simple plugin that returns a string Classifing the image provided Tensorflow.
included graph is traned to reconize [zombie, not zombie]


## Using

```js
    var success = function(message) {
        alert(message);
    }

    var failure = function() {
        alert("Error calling Tensorflow Plugin");
    }

    ATTensorflow.Classifier({Name:"out.bmp",Contrast:1,Brightness:1},success,failure);
```

### Classifier 

#### Needed

Name: Location In /sdcard/Pictures/ to read image from      
    defalut: "out.bmp"

Contrast: sets the image Contrast.
    defalut: 90

Brightness: sets the image Brightness.
    defalut: 10

#### Optional


MODEL_FILE: The filepath of the model GraphDef protocol buffer.
    defalut: "file:///android_asset/www/retrained_graph.pb"

LABEL_FILE: The filepath of label file for classes.
    defalut: "file:///android_asset/www/retrained_labels.txt"

NUM_CLASSES: The number of classes output by the model.
    defalut: 2

INPUT_SIZE: The input size. A square image of inputSize x inputSize is assumed.
    defalut: 299

IMAGE_MEAN: The assumed mean of the image values.
    defalut: 128

IMAGE_STD:  The assumed std of the image values.
    defalut: 128

INPUT_NAME:  The label of the image input node.
    defalut: "Mul:0"

OUTPUT_NAME: The label of the output node.
    defalut: "final_result:0"


## More Info

For more information on setting up Cordova see [the documentation](http://cordova.apache.org/docs/en/latest/guide/cli/index.html)

For more info on plugins see the [Plugin Development Guide](http://cordova.apache.org/docs/en/latest/guide/hybrid/plugins/index.html)

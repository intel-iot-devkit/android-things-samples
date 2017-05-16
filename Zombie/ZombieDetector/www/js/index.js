/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var pos = 0
var xAngle = 0, yAngle = 0;
var app = {
    // Application Constructor
    initialize: function() {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
        //document.getElementById("clickMe").addEventListener("click", TakePhoto);
    },

    // deviceready Event Handler
    //
    // Bind any cordova events here. Common events are:
    // 'pause', 'resume', etc.
    onDeviceReady: function() {
        this.receivedEvent('deviceready');
    },

    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    },
};

    var TakePhoto = function(){
        var image = document.getElementById('myImage').src = "";
        //ATmraa.BME280("Altitude", success, failure);
        //ATmraa.TMP006("C", success, failure);
        ATCamara.NoNo("",PhotoSuccess,failure);
    }

     var side = 0;

    var success = function(message) {
          value =  parseInt(message);
         //alert(message);
         console.log("Side : " +side+"  > "+ value);
         switch(side){
             case 0:
                    //Humidity
                  $("#PreviewGaugeMeter_2").data("percent",value);
                  $("#PreviewGaugeMeter_2").empty();
                  $("#PreviewGaugeMeter_2").gaugeMeter();
                  //setTimeout(function() { ATmraa.BME280("Humidity", success, failure); }, 5000);

                  break;
             case 1:
                //Altitude
                $("#PreviewGaugeMeter_5").data("percent",Math.abs(value));
                $("#PreviewGaugeMeter_5").empty();
                $("#PreviewGaugeMeter_5").gaugeMeter();
                //setTimeout(function() { ATmraa.BME280("Altitude", success, failure); }, 5000);
                break;
             case 2:
                //Pressure
                value = value/5000;
                $("#PreviewGaugeMeter_4").data("percent",value);
                $("#PreviewGaugeMeter_4").empty();
                $("#PreviewGaugeMeter_4").gaugeMeter();
                //setTimeout(function() { ATmraa.BME280("Pressure", success, failure); }, 5000);
                break;
             case 3:
                 //Temperature
                 //Math.abs(message)
                  $("#PreviewGaugeMeter_3").data("percent",parseInt(message));
                  $("#PreviewGaugeMeter_3").empty();
                  $("#PreviewGaugeMeter_3").gaugeMeter();
                  //setTimeout(function() { ATmraa.TMP006("C", success, failure); }, 5000);
                  break;
         }
    }

    var TensorflowSuccess = function(Classifcation) {
        document.getElementById("one").innerHTML = Classifcation;
    }

    var PhotoSuccess = function(imageData) {
         document.getElementById('myImage').src =  "data:image/jpeg;base64," + imageData;
         ATTensorflow.NoNo("",TensorflowSuccess,failure);
    }

    var failure = function( message ) {
        if(message == "IOException"){
            alert("Restart the TN_Shell");

        }else{
            alert("Error calling ATCamara Plugin");
        }
    }



    var RunSensor = function(){
            if(yAngle == 360 || yAngle == -360){
                yAngle = 0;
            }
            //console.log("yAngle: "+yAngle);
           // the side we're looking for -3 -2 -1 0 1 2 3
           side = (yAngle/90) % 4;
           //console.log(side);

       switch(side) {

        case 0:
            //Humidity
            ATmraa.BME280("Humidity", success, failure);
              side = 0;
            break;
        case 1:
        case -3:
            //Altitude
            side = 1;
            ATmraa.BME280("Altitude", success, failure);
            break;

        case -2:
        case 2:
            side = 2;
            //Pressure
            ATmraa.BME280("Pressure", success, failure);
            break;
        case -1:
        case 3:
            side = 3;
            //Temperature
            ATmraa.TMP006("C", success, failure);
            break;

       }
    }


var props = 'transform WebkitTransform MozTransform OTransform msTransform'.split(' '),
    prop,
    el = document.createElement('div');

for(var i = 0, l = props.length; i < l; i++) {
    if(typeof el.style[props[i]] !== "undefined") {
        prop = props[i];
        break;
    }
}


$('body').keydown(function(evt) {
    switch(evt.keyCode) {
        case 37: // left
            yAngle -= 90;
            RunSensor();
            break;

        case 39: // right
            yAngle += 90;
            RunSensor();
            break;

        case 38: // up
            xAngle = -90;
            yAngle = 0;
            TakePhoto();
            evt.preventDefault();
            break;



        case 40: // down
            if(xAngle == 0){
                xAngle = -90;
                yAngle = 0;
                TakePhoto();
            }else{
                xAngle = 0
            }
            evt.preventDefault();
            break;
    };
    document.getElementById('cube').style[prop] = "rotateX("+xAngle+"deg) rotateY("+yAngle+"deg)";
});


app.initialize();
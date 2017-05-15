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
        //ATmraa.RotaryEncoder("", roteryUpdateCuicle , function() {});
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


    var roteryUpdateCuicle = function(POS){
        roteryUpdate(POS);
    }

    var roteryUpdate = function(POS) {
        pos = POS;
        ATmraa.RotaryEncoder("", roteryUpdateCuicle , function() {});
     }

    var TakePhoto = function(){
        var image = document.getElementById('myImage').src = "";
        //ATmraa.BME280("Altitude", success, failure);
        //ATmraa.TMP006("C", success, failure);
        ATCamara.NoNo("",PhotoSuccess,failure);
    }

    var success = function(message) {
         alert(message);
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
            break;

        case 38: // up
            xAngle += 90;
            evt.preventDefault();
            break;

        case 39: // right
            yAngle += 90;
            break;

        case 40: // down
            xAngle -= 90;
            evt.preventDefault();
            break;

        case 13: // enter
            TakePhoto();
            evt.preventDefault();
            break;
    };
    document.getElementById('cube').style[prop] = "rotateX("+xAngle+"deg) rotateY("+yAngle+"deg)";
});


app.initialize();
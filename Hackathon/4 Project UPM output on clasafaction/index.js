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
var app = {
    // Application Constructor
    initialize: function() {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    // deviceready Event Handler
    onDeviceReady: function() {
        ATCamara.TakePhoto({Name:"out.bmp",Width:400,Height:400,Contrast:1,Brightness:1},PhotoSuccess,failure);
    
    },
};


    var PhotoSuccess = function(imageData) {
         document.getElementById('myImage').src =  "data:image/jpeg;base64," + imageData;
         ATTensorflow.Classifier({Name:"out.bmp",Contrast:1,Brightness:1},TensorflowSuccess,failure);

    }

    var TensorflowSuccess = function(Classifcation) {
        document.getElementById("myText").innerHTML = Classifcation;
        if(Classifcation.includes("not zombie")){
            //Not  A Zombie
            ATmraa.LED({pin:"J6_47",on:true,delay:2000},BUZZERSuccess,failure);
        }else{
            //A Zombie!!
            ATmraa.BUZZER({pin:"PWM_3",note:"FA",delay:500000},BUZZERSuccess,failure);
        }
    }


    var BUZZERSuccess = function( message ) {
        console.log(message);
    }

     var failure = function( message ) {
            if(message == "IOException"){
                console.log("Restart the TN_Shell");
            }else{
                console.log(message);
            }
     }
app.initialize();
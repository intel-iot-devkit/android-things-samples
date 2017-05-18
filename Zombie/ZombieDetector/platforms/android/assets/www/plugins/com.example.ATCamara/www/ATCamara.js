cordova.define("com.example.ATCamara.ATCamara", function(require, exports, module) {
/*global cordova, module*/

module.exports = {
    TakePhoto: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATCamara", "TakePhoto", [name]);
    }
};

});

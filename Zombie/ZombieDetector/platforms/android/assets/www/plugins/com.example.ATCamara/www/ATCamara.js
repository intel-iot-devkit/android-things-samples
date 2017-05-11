cordova.define("com.example.ATCamara.ATCamara", function(require, exports, module) {
/*global cordova, module*/

module.exports = {
    greet: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATCamara", "greet", [name]);
    },

    NoNo: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATCamara", "NoNo", [name]);
    }
};

});

/*global cordova, module*/

module.exports = {
    greet: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "greet", [name]);
    },

    TMP006: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "TMP006", [name]);
    },
    
    BME280: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "BME280", [name]);
    },

    RotaryEncoder: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "RotaryEncoder", [name]);
    },
};

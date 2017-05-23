/*global cordova, module*/

module.exports = {
    TMP006: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "TMP006", [name]);
    },
    
    BME280: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "BME280", [name]);
    },
    LED: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "LED", [name]);
    },
    BUZZER: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "BUZZER", [name]);
    }
};

/*global cordova, module*/

module.exports = {
    TMP006: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "TMP006", [name]);
    },
    
    BME280: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "BME280", [name]);
    }
};

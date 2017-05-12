/*global cordova, module*/

module.exports = {
    greet: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "greet", [name]);
    },

    NoNo: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "NoNo", [name]);
    }
};

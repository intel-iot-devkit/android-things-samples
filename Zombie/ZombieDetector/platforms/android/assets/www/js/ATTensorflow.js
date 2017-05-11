/*global cordova, module*/

module.exports = {
    greet: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATTensorflow", "greet", [name]);
    },

    NoNo: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATTensorflow", "NoNo", [name]);
    }
};

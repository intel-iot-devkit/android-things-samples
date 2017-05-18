/*global cordova, module*/

module.exports = {
    Classifier: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATTensorflow", "Classifier", [name]);
    }
};

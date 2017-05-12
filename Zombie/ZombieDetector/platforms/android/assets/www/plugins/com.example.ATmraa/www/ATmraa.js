cordova.define("com.example.ATmraa.ATmraa", function(require, exports, module) {
/*global cordova, module*/

module.exports = {
    greet: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "greet", [name]);
    },

    NoNo: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "ATmraa", "NoNo", [name]);
    }
};

});

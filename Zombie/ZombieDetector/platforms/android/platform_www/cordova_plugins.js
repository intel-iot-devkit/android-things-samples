cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "id": "com.example.ATCamara.ATCamara",
        "file": "plugins/com.example.ATCamara/www/ATCamara.js",
        "pluginId": "com.example.ATCamara",
        "clobbers": [
            "ATCamara"
        ]
    },
    {
        "id": "com.example.ATTensorflow.ATTensorflow",
        "file": "plugins/com.example.ATTensorflow/www/ATTensorflow.js",
        "pluginId": "com.example.ATTensorflow",
        "clobbers": [
            "ATTensorflow"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-whitelist": "1.3.2",
    "cordova-plugin-compat": "1.1.0",
    "com.example.ATCamara": "0.1.0",
    "com.example.ATTensorflow": "0.1.1"
};
// BOTTOM OF METADATA
});
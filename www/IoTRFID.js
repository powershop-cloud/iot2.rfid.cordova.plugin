var exec = require('cordova/exec');

exports.scanTag = function (arg0, success, error) {
    exec(success, error, 'IoTRFID', 'scanTag', [arg0]);
};

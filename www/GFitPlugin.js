var exec = require('cordova/exec');

function GFitPlugin() {
    console.log("GFitPlugin.js: is created");
}

// GFitPlugin.prototype.showToast = function(aString){
//     console.log("GFitPlugin.js: showToast");
//
//     exec(
//         function(result){
//             /*alert("OK" + reply);*/
//         },
//         function(result){
//             /*alert("Error" + reply);*/
//         },
//         "GFitPlugin", aString, []
//     );
// }

GFitPlugin.prototype.getStuff1 = function (startTime, endTime, datatypes, successCallback, failureCallback) {
    cordova.exec(successCallback,
        failureCallback,
        "GoogleFit",
        "getStuff1",
        [{
            "startTime" : startTime,
            "endTime" : endTime,
            "datatypes": datatypes
        }]);
    };

var GFitPlugin = new GFitPlugin();
module.exports = GFitPlugin;

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
    console.log("Calling exec...");
    cordova.exec(
        successCallback,
        failureCallback,
        "GFitPlugin",
        "getStuff1",
        [{
            "startTime" : startTime,
            "endTime" : endTime,
            "datatypes": datatypes
        }]
    );
    console.log("Called exec...");
};

var GFitPlugin = new GFitPlugin();
module.exports = GFitPlugin;

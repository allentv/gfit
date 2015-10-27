var exec = require('cordova/exec');

function GFitPlugin() { 
 console.log("GFitPlugin.js: is created");
}

GFitPlugin.prototype.showToast = function(aString){
 console.log("GFitPlugin.js: showToast");

 exec(function(result){
     /*alert("OK" + reply);*/
   },
  function(result){
    /*alert("Error" + reply);*/
   },"GFitPlugin",aString,[]);
}

 var GFitPlugin = new GFitPlugin();
 module.exports = GFitPlugin;

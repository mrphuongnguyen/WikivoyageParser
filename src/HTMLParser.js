// phantomjs
var page = require('webpage').create();
var system = require('system');
var args = system.args;
var nation;

// input ars
if (args.length === 1) {
  console.log('Try to pass some arguments when invoking this script!');
} else {
 nation = args[1];
 console.log("Document Name : " + args[1]);
 console.log("\n");
}

// url from nation
var url = 'http://en.wikivoyage.org/wiki/' + nation;

// page open and HTML file output
page.open(url, function (status) {
    var js = page.evaluate(function () {
        return document;
    });
    console.log(js.all[0].outerHTML);

    var fs = require('fs');

    try {
    	fs.write("/Users/kangdongho/Dev/EclipseWorkspace/WikivoyageParser/HTML/" + nation + ".html", js.all[0].outerHTML);
    } catch(e) {
    	console.log(e);
    }

    phantom.exit();
});
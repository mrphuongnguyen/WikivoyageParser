// phantomjs
var page = require('webpage').create();
var system = require('system');
var args = system.args;
var url;
var file;

// input ars
if (args.length === 1) {
  console.log('Try to pass some arguments when invoking this script!');
} else {
 url = args[1];
 file = args[2];
 console.log("Document Name : " + args[1]);
 console.log("\n");
}

// page open and HTML file output
page.open(url, function (status) {
    var js = page.evaluate(function () {
        return document;
    });
    console.log(js.all[0].outerHTML);

    var fs = require('fs');

    try {
    	fs.write("./wikipediaHtml/" + file + ".html", js.all[0].outerHTML);
    } catch(e) {
    	console.log(e);
    }

    phantom.exit();
});
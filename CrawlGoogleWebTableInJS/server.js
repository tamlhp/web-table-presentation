var https = require('https');
var fs = require('fs');
var express = require('express');
var app = express();

var downloadFolder = "download/"

// var file = fs.createWriteStream("file.csv");
// var request = http.get("https://www.google.com/fusiontables/exporttable?query=select%20*%20from%201RxymI_JagECSeYKNxW6cRhQ30nMncHKqidMSpi3h", function(response) {
//   response.pipe(file);
// });

//=========file==========
var writeFile = function(filename, data){
	console.log("write file: " + filename)
	fs.writeFileSync(filename, data)

	console.log(filename + " has been writed");
};

var readFile = function(filename, json){
	console.log("readfile : "  + (json ? "JSON":"TEXT") + "  :  " + filename );
	var data = fs.readFileSync(filename, 'utf8');

	if(json){
		return JSON.parse(jsonminify(data));
	}
	else{
		return data;
	}
	
};

var downloadFile = function(keyword, filename, url){
	mkdir(downloadFolder + keyword);
	var file = fs.createWriteStream(downloadFolder + keyword + "/" + filename);
	console.log("| downloading : " + filename);
	var request = https.get(url, function(response){
		response.pipe(file);
		console.log("> download file: " + filename + " : OK");
	})
};

var mkdir = function(dir){
	try{
		fs.mkdirSync(dir);
	}
	catch(e){

	}
};

var getSize = function(filename){
	var stat = fs.statSync(filename);
	return stat.size;
};

//=========read parameters============

var keywords = null;
var numberRequestTable = 100;
if(process.argv.length > 2){
    keywords = readFile(process.argv[2]).split("\n");
    console.log(keywords);
}

if(process.argv.length > 3){
	numberRequestTable = process.argv[3];
}

if(keywords == null){
	console.error("node server <keywords filename> [number of tables]")
	process.exit(1);
}

console.log("read parameters : OK");
//=========init environment=======
mkdir(downloadFolder);

console.log("init environment : OK");
console.log("data will be saved in : " + downloadFolder);
//=========server=========
app.get("/", function(req, res){
	res.send(JSON.stringify({keywords:keywords, numberTables: numberRequestTable}));
});

app.get("/download", function(req, res){
	var keyword = req.query.keyword;
	var url = req.query.url;
	var name = req.query.filename;
	console.log("keyword = " + keyword);
	console.log("url = " + url);
	console.log("name = " + name);
	downloadFile(keyword, name + ".csv", url);
	res.send("OK");
})


var server = app.listen(3000, function(){
	console.log("server is running at port: 3000")
})

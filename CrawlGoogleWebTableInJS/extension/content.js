var baseURL = "https://www.google.com/fusiontables/exporttable?query=select+*+from+";

var body = $("body");

var runBtn = $("<button class='run'>Run Script</button>").appendTo(body);
$("<button class='run' style='right:100px'>Clear Cookie</button>").appendTo(body).click(function(){
	cookieRemove('keyword');
	cookieRemove('keywords');
	cookieRemove('curKIndex');
	cookieRemove('curTIndex');
	cookieRemove('numberTables');
	cookieRemove("curIndex");
});

var keywords = cookieArrayGet("keywords", []);
var numberTables = cookieIntGet("numberTables", -1);
var keyword = cookieGet("keyword", "");
var curKIndex = cookieIntGet("curKIndex", -1) //keywords[curKIndex]
var curTIndex = cookieIntGet("curTIndex", 1000000); //table count [0 - numberTables]



runBtn.click(function(){
	$.ajax("http://localhost:3000",{
		dataType:"json",
		xhrFields:{withCredentials:false},
		success: function(data){
			console.log(data);
			keywords = data.keywords;
			numberTables = data.numberTables;

			cookieArraySet("keywords", keywords);
			cookieSet("numberTables", numberTables);

			run();
		},
		error:function(){
			console.error("Please check server");
			alert("Please run server.js first")
		}
	});
});


var run = function(){
	//new query
	console.log("curTIndex = " + curTIndex);
	console.log("curKIndex = " + curKIndex);
	console.log("keyword = " + keyword);
	if(curTIndex >= numberTables){
		debugger;
		newQuery();
	}
	else {
		

		setTimeout(readTables2, 1000);
	}
}

var newQuery = function(){
	curKIndex++;
	if(curKIndex >= keywords.length){
		//END run
		cookieRemove("keywords");
		cookieRemove("keyword");
		cookieRemove("numberTables");
		cookieRemove("curKIndex");
		cookieRemove("curTIndex");
		cookieRemove("curIndex");
	}
	else{
		curTIndex = 0;
		keyword = keywords[curKIndex];

		cookieSet("keyword", keyword);
		cookieSet("curKIndex", curKIndex);
		cookieSet("curTIndex", curTIndex);
		
		doNewQuery(keyword);
	}
}

var doNewQuery = function(keyword){
	var txt = $("#gbqfq");
	var btn = $(".gbqfi.gb_h");
	txt.val(keyword);
	btn.trigger("click");
}

var getCurRange = function(){
	var b = $(".desc").find("b");
	return [b[0].innerText, b[1].innerText];
};


var curState = 0;
var curRow = null;
var importLink = null;
var btnOk = null;
var curLink = null;
var curTimer = 0;
var curIndex = cookieGet("curIndex", 1);
var count = 0;
var range = getCurRange();
var rangeLength = range[1] - range[0] + 1;

var readTables2 = function(){
	curTimer = setInterval(function(){
		console.log("curIndex = " + curIndex, "  curState = " + curState, "  count = " + count, "  curTIndex = " + curTIndex, "  numberTables = " + numberTables);

		switch(curState){
			case 0:
			count = 0;
				curRow = document.getElementById("li_" + curIndex);
				if(curRow != null){
					curState = 1;
					// console.log(curRow);
				}
				else{
					break;
				}
			case 1:
				importLink = document.getElementById("imp_link_" + curIndex);
				if(importLink != null){
					curState = 2;
					// console.log(importLink);
					importLink.click();
					break;
				}
				else{
					break;
				}
			case 2:
				btnOk = document.getElementById("btn_ok_" + curIndex);
				if(btnOk != null){
					curState = 3;
					// console.log(btnOk);
					btnOk.click();
					break;
				}
				else{
					break;
				}
			case 3:
				count++;
				
				if(count > 10){
					console.log("too much fail > pass it");
					//too much fail, pass it
					curState = 0;
					curRow = null;
					curLink = null;
					importLink = null;
					btnOk = null;
					curIndex++;
					curTIndex++;
					cookieSet("curIndex", curIndex);
					cookieSet("curTIndex", curTIndex);
					location.reload();
					break;
				}
				curLink = document.getElementById("imp_" + curIndex);

				// console.log(curLink);
				if(curLink.innerText.indexOf("Successfully imported") >= 0){
					
					var a = curLink.getElementsByTagName("a")[0];

					console.log("------> ", a.href);
					var h3 = curRow.getElementsByTagName("h3")[0];
					console.log("------> " + h3.innerText);
					sendDataToDownload(keyword, curTIndex + "__" + h3.innerText, a.href);


					curState = 0;
					curRow = null;
					curLink = null;
					importLink = null;
					btnOk = null;
					curIndex++;
					curTIndex++;
					cookieSet("curIndex", curIndex);
					cookieSet("curTIndex", curTIndex);
					
					if(curTIndex >= numberTables){
						console.log("new query");
						clearInterval(curTimer);
						newQuery();
					}

					
					if(curIndex > rangeLength){
						//TODO click next page
						console.log("next page");
						cookieRemove("curIndex");
						clearInterval(curTimer);
						var next = document.getElementById("navbar").getElementsByClassName("b")[0].getElementsByTagName("a")[0];
						next.click();

					}

				}
				else{
					break;
				}
		}
		
	}, 1000);
}


var sendDataToDownload = function(keyword, filename, url){
	//http://www.google.com/fusiontables/data?docid=17Ke7APnOQZSxXOn1SmOvVmRSTjgO5yUvPVi767_Q&ei=1t0nU4P4FYfWqAHz-IHgAg
	var id = getParameter(url, "id");
	url = baseURL + id;
	$.ajax("http://localhost:3000/download", {
		data:{
			keyword: keyword,
			filename:filename,
			url: url
		},
		xhrFields:{withCredentials:false},
		success:function(data){
			console.log("sendDataToDownload: " + data);
		}
	})

};


if(keywords.length > 0){
	run();
}

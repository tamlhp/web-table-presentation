//===========Cookie===========
var cookieGet = function(key, defaultVal){
	var data = $.cookie(key);
	if(typeof data == 'undefined'){
		return defaultVal;
	}
	else{
		return data;
	}
};

var cookieArrayGet = function(key, defaultVal){
	var str = $.cookie(key);
	if(typeof str == "undefined"){
		return defaultVal;
	}
	else{
		return str.split(",");
	}
};

var cookieIntGet = function(key, defaultVal){
	var data = $.cookie(key);
	if(typeof data == 'undefined'){
		return defaultVal;
	}
	else{
		return parseInt(data);
	}
}

var cookieSet = function(key, val){
	$.cookie(key, val, {path:"/", expires:365});
};

var cookieArraySet = function(key, val){
	var str = val.toString();
	cookieSet(key, str);
}

var cookieRemove = function(key){
	$.removeCookie(key);
}

var getParameter = function(url, paramname){
    var index = url.indexOf("?");
    if(index >= 0){
        index = url.indexOf(paramname, index);
        if(index > 0){
            var result = "";
            for(var i = index + paramname.length + 1; i < url.length; i++){
                var c = url[i];
                if(c != '&'){
                    result += c;
                }
                else{
                    break
                }
            }
            if(result.length > 0){
                return result;
            }
            else{
                return null;
            }
        }
    }

    return null;
};

var sleep = function(milisecon){
	console.log("sleep");
	var now = new Date();
	var tick = new Date();
	while(tick - now < milisecon){
		tick = new Date();
	}
	console.log("end sleep");
}
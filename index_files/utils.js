/**
 * Created by Tuan Chau on 12/20/13.
 */
/**
 *
 * @param request contains only number, string, boolean, array of number or string. Otherwise, skip
 * @returns {string}
 */
var toUrlParams = function (request) {
	var s = "";
	for (k in request) {
		var val = request[k];
		var type = typeof val;
		if (type == 'number' || type == 'string' || type == 'boolean') {
			s += k + "=" + val + '&';
		}
		else if (type == 'object' && val.length && val.length > 0) {
			s += k + "=" + val[0];
			for (var i = 1; i < val.length; i++) {
				s += "," + val[i];
			}
			s += "&";
		}
	}

	return s.substr(0, s.length - 1);
};

var compareNumber = function (a, b) {
	return a - b;
};

var compareNumberDesending = function (a, b) {
	return b - a;
};

var compareString = function (a, b) {
	return a.localeCompare(b);
};

var compareStringDesending = function (a, b) {
	return b.localeCompare(a);
};

var sort = function (array, attr, descending) {
	if (typeof descending === 'undefined') {
		descending = false;
	}
	if(!('length' in array) || array.length == 0){
		return [];
	}
	var compare = null;
	if (typeof array[0][attr] == 'number') {
		compare = descending ? compareNumberDesending : compareNumber;
	}
	else {
		compare = descending ? compareStringDesending : compareString;
	}
	var result = new Array(array.length);
	for (var i = 0, l = array.length; i < l; i++) {
		var ni = array[i];
		result[i] = ni;
		for (var j = i - 1; j >= 0; j--) {
			var nj = result[j];
			if (compare(ni[attr], nj[attr]) < 0) {
				result[j] = ni;
				result[j + 1] = nj;
			}
			else {
				break;
			}
		}

	}
//	for (var i = 0; i < result.length; i++) {
//		console.log(result[i][attr]);
//	}
	return result;
};

/**
 * merge two objects together
 * @param {Object} a
 * @param {Object} b
 * @return {Object}
 */
var merge = function(a, b){
	var result = {};
	for(var k in a){
		result[k] = a[k];
	}
	for(var k in b){
		result[k] = b[k];
	}
	
	return result;
};

var arrayToObject = function (array, attribute) {
	var result = {};
	if (attribute) {
		for (var i = 0, l = array.length; i < l; i++) {
			var child = array[i];
			result[child[attribute]] = child;
		}
	}
	else {
		for (var i = 0, l = array.length; i < l; i++) {
			var k = array[i];
			result[k] = k;
		}
	}

    return result;
};

var objectToArray = function(object){
    var result = [];
    for(var k in object){
        if(object.hasOwnProperty(k)){
            result.push({key:k, value:object[k]});
        }
    }

    return result;
};

var countAttributes = function(object){
	var count = 0;
	for(var k in object){
		if(object.hasOwnProperty(k)){
			count++;
		}
	}
	
	return count;
};

var compareDate = function(dateVal1, dateVal2){
    var d1 = new Date(dateVal1);
    var d2 = new Date(dateVal2);
    if(d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate()){
        return 0;
    }
    else{
        return d1 - d2;
    }
};

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
};

var compareDate2 = function(date1, date2){
    if(date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth() && date1.getDate() == date2.getDate()){
        return 0;
    }
    else{
        return date1 - date2;
    }
};

var getColor = function(relevant){
    //#076600
    //#0A8300
    //#0CA300
    //#2CB01C
    //#41BA2F
    //#78D261
    //#ACE892
//    if(relevant >= 0.85){
//        return "#076600";
//    }
    if(relevant >= 0.7){
        return "#0A8300";
    }
    if(relevant >= 0.6){
        return "#0CA300";
    }
    if(relevant >= 0.5){
        return "#2CB01C";
    }
    if(relevant >= 0.4){
        return "#41BA2F";
    }
    if(relevant >= 0.3){
        return "#78D261";
    }

    return "#ACE892";
};

var getColor2 = function(relevant){
    if(relevant >= 0.85){
        return "#ACE892";
    }
    if(relevant >= 0.7){
        return "#78D261";
    }
    if(relevant >= 0.6){
        return "#41BA2F";
    }
    if(relevant >= 0.5){
        return "#2CB01C";
    }
    if(relevant >= 0.4){
        return "#0CA300";
    }
    if(relevant >= 0.3){
        return "#0A8300";
    }

    return "#0A8300"
//    return "#076600";
};

var fillZero = function(ext, number){
    if(!number){
        number = 3;
    }
    ext = "" + ext;
    for(var i = 0, l = number - ext.length; i < l; i++){
        ext = '0' + ext;
    }

    return ext;
};

var numberToString = function(number){
    var minus = false;
    if(number < 0){
        minus = true;
        number = -number;
    }
    var ext = number % 1000;
    number = Math.floor(number/1000);

    var result = "" + number > 0?fillZero(ext):ext;
    while(number > 0){
        ext = number % 1000;
        number = Math.floor(number/1000);
        result = (number > 0?fillZero(ext):ext) + "," + result;
    }

    if(minus){
        result = "-" + result;
    }

    return result;
};

if (!String.prototype.format) {
	String.prototype.format = function () {

		var args = arguments;
		var sprintfRegex = /\{(\d+)\}/g;

		var sprintf = function (match, number) {
			return number in args ? args[number] : match;
		};

		return this.replace(sprintfRegex, sprintf);
	};
}
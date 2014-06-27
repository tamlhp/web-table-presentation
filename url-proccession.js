/**
 * Created by tuanchauict on 3/16/14.
 */

function URLProccession(layouts){
//    var google3 = layouts.googleLayout3;
//    var google2 = layouts.googleLayout2;
//    var google1 = layouts.googleLayout1;
//    var newLayout3 = layouts.newLayout3;
//    var newLayout2 = layouts.newLayout2;
//    var newLayout1 = layouts.newLayout1;

    var googleLayout = null;
    var newLayout = null;

    this.run = function(){
        var url = document.URL;
        var type = getParameter(url, "type");
        switch (type){
            case "1":
                googleLayout = layouts.googleLayout1;
                newLayout = layouts.newLayout1;
                break;
            case "2":
                googleLayout = layouts.googleLayout2;
                newLayout = layouts.newLayout2;
                break;
            case "3":
                googleLayout = layouts.googleLayout3;
                newLayout = layouts.newLayout3;
                break;
            default :
                googleLayout = layouts.googleLayout1;
                newLayout = layouts.newLayout1;
                break;
        }

        googleLayout.init({
            container:"#google-layout"
        });
        newLayout.init({
            container:"#new-layout"
        });
        var dataId = getParameter(url, "data_id");

        if(dataId){
            var dataname = "data" + dataId + ".json";
            var data = $.ajax(dataname, {
                type:'GET',
                dataType:'text',
                xhrFields:{withCredentials:false},
                success:onRequestDataSuccess,
                error:function(err){
                    console.error(err);
                }
            });
        }
    };


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

    var onRequestDataSuccess = function(data){
        data = JSON.minify(data);
        data = JSON.parse(data);

        if(data.keyword && data.oldLook && data.newLook){
            data.newLook.keyword = data.oldLook.keyword = data.keyword;
            normalize(data.oldLook, true);
            normalize(data.newLook, false);

            googleLayout.setData(data.oldLook);
            newLayout.setData(data.newLook);

        }
        console.log(data);
    };


    var normalize = function(look, isOld){
        look.result = setDefault(look.result, {"from":1, "to":10, "total":Math.floor(100000 * Math.random() + 99999)});
        look.result.from = setDefault(look.result.from, 1);
        look.result.to = setDefault(look.result.to, 10);
        look.result.total = setDefault(look.result.total, Math.floor(100000 * Math.random() + 99999));

        var tables = look.tables;
        for(var i = 0; i < tables.length; ++i){
            var t = tables[i];
            t.tops = setDefault(t.tops, isOld?[0]:[1,3,10]);
            t.hasTitle = setDefault(t.hasTitle, true);

        }
    };

    var setDefault = function(value, defaultValue){
        if(typeof value == "undefined"){
            return defaultValue;
        }
        else{
            return value;
        }
    }

}


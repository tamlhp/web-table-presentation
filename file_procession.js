/**
 * Created by tuanchauict on 3/11/14.
 */

function FileProcession(googleLayout, newLayout){
    this.init = function(){
        if (window.File && window.FileReader && window.FileList && window.Blob){
            var reader = new FileReader();
            reader.onload = onFileLoad;
            $("#file").change(function (e) {
                var files = e.target.files;
                console.log(files[0].type + "   " + files[0].name);
                if(files.length > 0){
                    reader.readAsText(files[0]);

                }
            })

        }
        else{
            alert("Your browser does not support file reader");
        }

        return this;
    };

    var handleFileSelect = function(file){

    };

    var onFileLoad = function(data){
//        console.log(data);
        data = JSON.minify(data.target.result);
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
};




/**
 * Created by tuanchauict on 3/11/14.
 */
function GoogleLayout(){

    var container;
    var searchbox;
    var searchCount;
    var searchBody;

    var rand = new Random();


    this.init = function(){
        container = $("#google-layout");
        searchbox = container.find(".google-txt");
        searchCount = container.find(".google-result-count");
        searchBody = container.find(".google-body");


        searchBody.click(function(e){
            var target = e.target;
            var cls = target.className;
            console.log(cls)

            switch (cls){
                case "more":
                    onShowMoreClicked(target);
                    break;
                case "google-show-more":
                    onShowMoreClicked(target.getElementsByClassName('more')[0])
                    break;

                default :

                    break;
            }

        });
        return this;
    };

    var onShowMoreClicked = function(target){
        target = $(target);
        var table = target[0].parentNode.parentNode.getElementsByClassName("google-table-instant")[0];
        console.log(target.innerText);
        if(target.text() == "more"){
            target.text("less");
//            target.innerText = "less";
            table.style.display = "";
        }
        else{
            target.text("more");
//            target.innerText = "more";
            table.style.display = "none";
        }
    };

    this.setData = function(data){
        searchbox.text(data.keyword);
        var result = data.result;
        searchCount.html("Results <b>{0}</b> - <b>{1}</b> of about <b>{2}</b> for <b>{3}</b>. (0.12 seconds)"
            .format(numberToString(result.from), numberToString(result.to), numberToString(result.total), data.keyword));

        var html = "";
        var tables = data.tables;
        for(var i = 0; i < tables.length; i++){
            html += buildTable(tables[i], data.keyword, i > 0);
        }
        searchBody.html(html);
    };

    var buildTable = function(table, keyword, hide){
        var rows = table.rows;
        var top = table.tops[0];
        if(top == 0){
            top = rows.length;
        }
        top = table.hasTitle?top + 1: top;

        var row = rows[0];
        if(top > rows.length){
            top = rows.length;
        }
        var result = ('<div class="google-table">' +
            '<a class="google-table-title" href="{0}">{1}</a>' +
            '<div class="google-cite">{2}</div>' +
            '<div class="google-show-more">Show <span class="more">{3}</span> ({4} rows / {5} columns total) - Import data</div>' +
            '<table class="google-table-instant" style="display: {6}">' +
            '<tbody>')
            .format((isUrl(table.url)?"":"http://") + table.url,
                makeBold(table.title, keyword),
                makeBold(table.url, keyword),
                hide?"more":"less",
                numberToString(rows.length + rand.nextInt()),
                numberToString(row.length),
                hide?"none":""
            );

        for(var i = 0; i < top; i++){
            row = rows[i];
            result += "<tr>";
            for(var j = 0; j < row.length; j++){
                result += "<td>" + row[j] + "</td>";
            }

            result += "</tr>";
        }

        result += "</tbody></table></div>";
        return result;
    };

    var makeBold = function(text, keyword){
        keyword = keyword.toLowerCase();

        return text;
    };




}
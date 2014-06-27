/**
 * Created by tuanchauict on 3/11/14.
 */
function NewLayout(){


    var container;
    var searchbox;
    var searchCount;
    var searchBody;


    this.init = function(){
        container = $("#new-layout");
        searchbox = container.find(".google-txt");
        searchCount = container.find(".google-result-count");
        searchBody = container.find(".google-body");


        searchBody.click(function(e){
            var target = e.target;
            var cls = target.className;
            console.log(cls)
            if(cls != 'new-tops' && cls.indexOf("new-top") >= 0){
                var parent = target.parentNode;
                var tops = parent.getElementsByClassName("new-top");
                console.log(tops);
                for(var i = 0; i < tops.length; i++){
                    tops[i].className = "new-top";
                }
                target.className = "new-top new-top-active";
                var top = target.getAttribute('data-value');

                var rows = parent.parentNode.getElementsByClassName("google-table-instant")[0].getElementsByTagName('tbody')[0].getElementsByTagName('tr');
                for(i = 0; i < rows.length; i++){
                    rows[i].style.display = i >= top?"none":"";
                }
            }

        });
        return this;
    };

    var onShowMoreClicked = function(target){
        var table = target.parentNode.parentNode.getElementsByClassName("google-table-instant")[0];
        if(target.innerText == "more"){
            target.innerText = "less";
            table.style.display = "";
        }
        else{
            target.innerText = "more";
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
            html += buildTable(tables[i], data.keyword);
        }
        searchBody.html(html);
    };

    var buildTable = function(table, keyword){
        var rows = table.rows;
        var row = rows[0];
        var tops = table.tops;
        var header = table.hasTitle?1:0;
        var topNames = "";
        for(var i = 0; i < tops.length; i++){
            if(tops[i] + header >= rows.length){
                tops[i] = -1;
            }
            else{
                topNames += '<div class="new-top {0}" data-value="{1}">Top {2}</div>'.format(i == 0?"new-top-active":"", tops[i] + header, tops[i]);
                tops[i] += header;
            }
        }
        topNames += '<div class="new-top" data-value="{0}">Full ({1} rows)</div>'.format(rows.length, rows.length - header);

        var result = ('<div class="google-table">' +
            '<a class="google-table-title" href="#{0}">{1}</a>' +
            '<div class="google-cite">{2}</div>' +
            '<div class="new-tops">' +
//            '<div class="google-show-more">Show <span class="more">more</span> ({3} rows / {4} columns total) - Import data</div>' +
            topNames +
            '</div>' +
            '<table class="google-table-instant">' +
            '<tbody>')
            .format(Math.random(),
                makeBold(table.title, keyword),
                makeBold(table.url, keyword),
                numberToString(rows.length),
                numberToString(row.length)

            );

        for(var i = 0; i < rows.length; i++){
            row = rows[i];
            result += "<tr data-value='{0}' style='{1}'>".format(i, i >= tops[0] ? "display:none":"");
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
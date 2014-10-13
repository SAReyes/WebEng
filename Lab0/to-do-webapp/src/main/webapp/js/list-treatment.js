// setup list.js
var options = {
    valueNames: [ "tTask", "tContext", "tProject", "tPriority" ]
};

var columnFilter = "tAll"
var taskListJS = new List("to-do-tasks", options);

// Functions
function textWrittenOnSearch(){
    var e = document.getElementById("searchBox");
    var text = e.value;
    searchText(text);
}

function columnFilterChanged(){
    var e = document.getElementById("columnFilterSpinner");
    columnFilter = e.options[e.selectedIndex].value;
    textWrittenOnSearch();
}

function priorityFilterChanged(){
    var e = document.getElementById("searchFilterPriority");
    var selection = e.options[e.selectedIndex].innerHTML;
    if(selection === "All"){
        taskListJS.filter();
    } else{
        taskListJS.filter(function (item){
            if(item.values().tPriority == selection){
                return true;
            } else {
                return false;
            }
        });
    }
}

function searchText(text){
    if(columnFilter === "tAll"){
        taskListJS.search(text,options.valueNames);
    } else {
        taskListJS.search(text,[columnFilter]);
    }
}

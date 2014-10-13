function saveNewTask() {
    var name = document.getElementById("newTaskName").value;
    var context = document.getElementById("newContext").value;
    var project = document.getElementById("newProject").value;
    var e = document.getElementById("newPriority");
    var priority = e.options[e.selectedIndex].innerHTML;

    context = (context==="") ? "-" : context;
    project = (project==="") ? "-" : project
    while(name === ""){
        name = prompt("A name is needed:");
    }
    if(priority === "Priority"){
        alert("No priority selected\nUsing default(Medium)")
        priority = "Medium"
    }

    taskListJS.add({
        tTask: name,
        tContext: context,
        tProject: project,
        tPriority: priority
    });

    // AJAX Setup
    var xmlhttp;
    if (window.XMLHttpRequest){
        // IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    } else{
        // IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }

    var params = "?taskName=" + name + "&taskContext=" + context + "&taskProject="
                    + project + "&taskPriority=" + priority;
    xmlhttp.open("GET","http://" + window.location.host + "/saveTodoTask" + params,true);
    xmlhttp.send();
}
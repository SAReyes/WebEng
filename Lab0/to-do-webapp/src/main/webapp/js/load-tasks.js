function loadTasks(){
    // AJAX Setup
    var xmlhttp;
    if (window.XMLHttpRequest){
        // IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    } else{
        // IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    // On server response
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var jsonData = JSON.parse(xmlhttp.responseText);

            writeTasks(jsonData.taskList);
        }
    }

    xmlhttp.open("GET","http://" + window.location.host + "/loadTodoTasks",true);
    xmlhttp.send();
}

function writeTasks(taskList){
    var i,context,project;

    // Remove the template on load
    taskListJS.clear();
    // Disable ajax loader
    document.getElementById("ajaxLoader").style.display = "none";
    document.getElementById("to-do-tasks").style.display = "block";

    // Read backwards because of scala's list implementation
    for(i = taskList.length-1; i>=0; i--){
        console.log(taskList[i].context);

        context = (taskList[i].context === null) ? "-" : taskList[i].context;
        context = (taskList[i].project === null) ? "-" : taskList[i].project;

        taskListJS.add({
            tTask: taskList[i].name,
            tContext: context,
            tProject: taskList[i].project,
            tPriority: taskList[i].priority
        });
    }
}
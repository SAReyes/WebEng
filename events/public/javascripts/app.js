var myapp = angular.module('myapp', []);

myapp.controller('BodyCtrl', function ($scope) {
    $scope.tasks = [];

    /* Setting up all dropdowns */
    $scope.priorityOptions = [
        {name: "Priority", value: "Priority"},
        {name: "LOW", value: "LOW"},
        {name: "MEDIUM", value: "MEDIUM"},
        {name: "HIGH", value: "HIGH"}
    ];
    $scope.taskPriority = {priority: $scope.priorityOptions[0].value};

    $scope.columnFilterOptions = [
        {name: "All", value: "$"},
        {name: "Task", value: "name"},
        {name: "Context", value: "context"},
        {name: "Project", value: "project"}
    ];
    $scope.columnFilter = $scope.columnFilterOptions[0].value;

    $scope.priorityFilterOptions = [
        {name: "All", value: ""},
        {name: "LOW", value: "LOW"},
        {name: "MEDIUM", value: "MEDIUM"},
        {name: "HIGH", value: "HIGH"}
    ];
    $scope.priorityFilter = $scope.priorityFilterOptions[0].value;

    $scope.searchText = {name: "", context: "", project: "", priority: ""};

    /* WebSocket setup */
    var ws_uri = "ws://" + window.location.host + "/ws/tasks";

    var ws = new WebSocket(ws_uri);

    ws.onopen = function () {
        ws.send(JSON.stringify({op: "get_json"}));
    };

    ws.onmessage = function (e) {
        var response = JSON.parse(e.data);

        switch (response.op) {
            case "full_list":
                $scope.$apply(function () {
                    $scope.tasks = response.todoTaskList.taskList.reverse();
                });
                break;
            default:
                console.log(response);
                break;
        }
    };

    /* watchers to follow up the dropdown filters */
    $scope.$watch('columnFilter', function () {
        if ($scope.columnFilter === "name") {
            $scope.searchText.name = getSearchText();
            $scope.searchText.$ = "";
            $scope.searchText.context = "";
            $scope.searchText.project = "";
        }
        else if ($scope.columnFilter === "context") {
            $scope.searchText.context = getSearchText();
            $scope.searchText.$ = "";
            $scope.searchText.name = "";
            $scope.searchText.project = "";
        }
        else if ($scope.columnFilter === "project") {
            $scope.searchText.project = getSearchText();
            $scope.searchText.$ = "";
            $scope.searchText.name = "";
            $scope.searchText.context = "";
        }
        else if ($scope.columnFilter === "$") {
            $scope.searchText.$ = getSearchText();
            $scope.searchText.name = "";
            $scope.searchText.context = "";
            $scope.searchText.project = "";
        }

        function getSearchText() {
            if ($scope.searchText.name !== "") {
                return $scope.searchText.name;
            }
            else if ($scope.searchText.context !== "") {
                return $scope.searchText.context;
            }
            else if ($scope.searchText.project !== "") {
                return $scope.searchText.project;
            }
            else if ($scope.searchText.$ !== "") {
                return $scope.searchText.$;
            }
        }
    });

    $scope.$watch('priorityFilter', function () {
        if ($scope.priorityFilter === undefined) {
            $scope.searchText.priority = "";
        }
        else if ($scope.priorityFilter === "LOW") {
            $scope.searchText.priority = "LOW";
        }
        else if ($scope.priorityFilter === "MEDIUM") {
            $scope.searchText.priority = "MEDIUM";
        }
        else if ($scope.priorityFilter === "HIGH") {
            $scope.searchText.priority = "HIGH";
        }
    });

    /* actions performed from the view */
    $scope.saveNewTask = function () {
        var task = {
            name: $scope.taskName,
            context: $scope.taskContext,
            project: $scope.taskProject,
            priority: $scope.taskPriority.priority
        };
        var request = {
            op: "add_task",
            task: task
        };
        ws.send(JSON.stringify(request));
    };

    $scope.removeTask = function (task) {
        var request = {
            op: "delete_task",
            task: task
        };
        ws.send(JSON.stringify(request));
    };
});
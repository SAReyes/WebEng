var myapp = angular.module('myapp', []);

myapp.controller('BodyCtrl', function ($scope, $http) {
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

    /* data setup */
    $http.get('http://' + window.location.host + '/todoList/get')
        .success(function(data){
            $scope.tasks = data.taskList.reverse();
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
            _id: 0,
            name: $scope.taskName,
            context: $scope.taskContext,
            project: $scope.taskProject,
            priority: $scope.taskPriority.priority
        };
        $http.post('http://' + window.location.host + '/todoList/post',JSON.stringify(task))
            .success(function(data){
                $scope.tasks.push(data);
            })
    };

    $scope.removeTask = function (task) {
        $http.delete('http://' + window.location.host + '/todoList/delete/'+ task._id)
            .success(function(){
                $scope.tasks = $scope.tasks.filter( function(x){return x._id !== task._id} );
            })
    };
});
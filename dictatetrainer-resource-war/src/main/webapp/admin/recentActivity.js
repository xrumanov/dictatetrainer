angular.module('DictateTrainer')
    .controller('RecentActivityCtrl', function ($scope, $http) {

        $http.get("/api/users?sort=-createdAt")
            .success(function (response) {
                $scope.users = response.entries;
            });

        $http.get("/api/dictates?sort=-id")
            .success(function (response) {
                $scope.dictates = response.entries;
            });

        $http.get("/api/categories?sort=-id")
            .success(function (response) {
                $scope.categories = response.entries;
            });

        $http.get("/api/trials?sort=-performed")
            .success(function (response) {
                $scope.trials = response.entries;
            });

        $http.get("/api/schools?sort=-id")
            .success(function (response) {
                $scope.schools = response.entries;
            });

        $http.get("/api/schoolclasses?sort=-id")
            .success(function (response) {
                $scope.schoolClasses = response.entries;
            });



        //functions for buttons - trial details
        $scope.showTrial = function (id) {
            $location.path("/t-edit-d/" + id);
        };

        ////functions for buttons - student management
        //$scope.manageUser = function (id) {
        //    $location.path("/t-edit-u/" + id);
        //};
        //
        ////functions for buttons - student statistics
        //$scope.statsUser = function (id) {
        //    $location.path("/t-stats-u/" + id);
        //};
    });

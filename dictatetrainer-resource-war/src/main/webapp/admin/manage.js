angular.module('DictateTrainer')
    .controller('ManagementCtrl', function ($scope, $location, $http) {

        $http.get("/api/users?sort=id")
            .success(function (response) {
                $scope.users = response.entries;
            });

        $http.get("/api/dictates?sort=id")
            .success(function (response) {
                $scope.dictates = response.entries;
            });

        $scope.manageDictate = function (id) {
            $location.path("/edit-d/" + id);
        };

        $scope.manageUser = function (id) {
            $location.path("/edit-u/" + id);
        }
    });

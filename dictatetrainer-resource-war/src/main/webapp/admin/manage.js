angular.module('DictateTrainer')
    .controller('ManagementCtrl', function ($scope, $location, $http, userService, dictateService) {

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
        };

        $scope.deleteDictate = function (id) {
            $http.delete("/api/dictates/"+id)
                .success(function () {
                    // on delete dictate success
                    $scope.error = "";
                    $scope.success = "Smazání proběhlo úspěšně!";
                    $location.path("#/mng-d");
                }).error(function(response){
                    // on delete dictate error
                    $scope.error = response.errrorIdentification + " " + response.errorDescription;
                    $scope.success = "";
                });
        };

        $scope.deleteUser = function (id) {
            $http.delete("/api/users/"+id)
                .success(function () {
                    // on delete user success
                    $scope.error = "";
                    $scope.success = "Smazání proběhlo úspěšně!";
                    $location.path("#/mng-u");
                }).error(function(response){
                    // on delete user error
                    $scope.error = response.errrorIdentification + " " + response.errorDescription;
                    $scope.success = "";
                });
        };
    });

angular.module('DictateTrainer')
    .controller('EditUserCtrl', function ($scope, $http, $routeParams) {

        var idParam = $routeParams.id;

        $http.get("/api/users/"+idParam)
            .success(function (response) {
                $scope.userToEdit = response;
            });

        $scope.submitUser = function (id) {
            $http.update("/api/users/"+idParam)
                .success(function (response) {
                    $scope.updatedUser = response;
                });
        }
    });

angular.module('DictateTrainer')
    .controller('EditUserCtrl', function ($scope, $http, $routeParams) {

        var idParam = $routeParams.id;

        $scope.onInit = function () {
            $http.get("/api/users/" + idParam)
                .success(function (response) {
                    $scope.userToEdit = response;
                });
        };


        $scope.updateUser = function (id) {
            $http.put("/api/users/" + id, {
                name: $scope.userToEdit.name,
                email: $scope.userToEdit.email,
                type: $scope.userToEdit.type,
                schoolClassId: $scope.userToEdit.schoolClass.id
            })
                .success(function (response) {
                    $scope.updatedUser = response;
                });
        }
    });

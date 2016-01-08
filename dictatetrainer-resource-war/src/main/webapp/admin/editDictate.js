angular.module('DictateTrainer')
    .controller('EditDictateCtrl', function ($scope, $http, $routeParams) {

        var idParam = $routeParams.id;

        $scope.dictateToEdit = {};

        $http.get("/api/dictates/"+idParam)
            .success(function (response) {
                $scope.dictateToEdit = response;
            });

        $scope.submitDictate = function (id) {
            $http.update("/api/dictates/"+idParam)
                .success(function (response) {
                    $scope.updatedDictate = response;
                });
        }
    });
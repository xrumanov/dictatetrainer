angular.module('DictateTrainer')
    .controller('ErrorDetailsCtrl', function ($scope, $http, $routeParams) {

        var idParam = $routeParams.id;

        $http.get("/api/errors/"+idParam)
            .success(function (response) {
                $scope.error = response;
            });
    });
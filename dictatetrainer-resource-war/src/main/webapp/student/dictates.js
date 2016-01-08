angular.module('DictateTrainer')
    .controller('DictatesCtrl', function ($scope, $http, dictateService) {

        $http.get("/api/dictates")
            .success(function (response) {
                $scope.dictates = response.entries;
            });

        $scope.dictate = dictateService.get( {filename: 'sample.ogg'}  );    // GET

        //ill wait for the angular to get the data form rest
        $scope.dictate.$promise.then(function(data) {
            console.log(data);
        });
    });
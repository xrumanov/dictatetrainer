angular.module('DictateTrainer')
    .controller('StatisticsCtrl', function ($scope, $rootScope, $http, dictateService, trialService, $location) {

        $scope.onInit = function () {
            $scope.studentId = $rootScope.globals.currentUser.id;
            $scope.studentName = $rootScope.globals.currentUser.name;
            $scope.studentTrainedDictates = {};
            $scope.trials = {};

            trialService.get({studentId: $scope.studentId}).$promise.then(function (response) {
                //on trial get success
                $scope.trials = response.entries;
                //create set of dictate ids
                var dictateIds = new Set([]);

                //extract it from the response
                for (var i = 0; i < response.entries.length; i++) {
                    dictateIds.add(response.entries[i].dictate.id);
                }

                // for each value in the set
                dictateIds.forEach(function (value, key, setObj) {
                    var j = 0;
                    trialService.get({dictateId: value}).$promise.then(function (response) {
                        //on success getting dictate by id add to variable
                        $scope.studentTrainedDictates[j] = response.entries[0].dictate;
                        j++;
                    }, function (response) {

                    })
                });

            }, function (response) {
                // on trial get failure
                $scope.error = response.errorIdentification + " " + errorDescription;
            })
        };

        $scope.trialDetails = function(id) {
            $location.path("/error-list/trials/"+id);
        }
    });
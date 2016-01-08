angular.module('DictateTrainer')
    .controller('CreateSchoolCtrl', function ($scope, schoolService, $location) {

        $scope.school = {name: ''};

        $scope.submitSchool = function () {
            schoolService.save({name: $scope.school.name}).$promise.then(function (response) {
                // onSuccess
                $scope.success = "Škola úspěšně vytvořena!";
                $scope.school = {};
            }, function (response) {
                //onError
                $scope.error = response.data.errorDescription;
            })
        }
    });


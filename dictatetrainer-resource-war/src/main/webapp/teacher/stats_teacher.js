angular.module('DictateTrainer')
    .controller('TeacherStatsCtrl', function ($scope, $rootScope, $http, dictateService, userService, $location) {

        $scope.onInit = function () {
            $scope.dictates = {};
            $scope.myStudents = {};
            $scope.errorsByStudents = {};

            // get id of logged teacher
            var teacherId = $rootScope.globals.currentUser.id;

            // get dictate of teacher
            dictateService.get({teacherId: teacherId})
                .$promise.then(function (response) {
                    dictates = response.entries[0];

                });
        };


    });
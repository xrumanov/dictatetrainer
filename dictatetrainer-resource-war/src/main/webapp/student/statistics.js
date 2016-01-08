angular.module('DictateTrainer')
    .controller('StatisticsCtrl', function ($scope, $rootScope, $http) {


        var studentId = $rootScope.globals.currentUser.id;

        $http.get("/api/trials?userId=" + studentId)
            .success(function (response) {
                $scope.trials = response.entries;
            });
    });
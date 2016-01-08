angular.module('DictateTrainer')
    .controller('ProfileCtrl', function($scope, $rootScope) {

        $scope.user = {
            name : $rootScope.globals.currentUser.name,
            email : $rootScope.globals.currentUser.email
        };

        $scope.editUser = function() {

        }
        

    });
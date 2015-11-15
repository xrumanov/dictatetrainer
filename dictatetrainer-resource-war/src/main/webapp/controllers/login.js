angular.module('DictateTrainer')
    .controller('LoginCtrl', function($scope, $rootScope, $location, authService) {
        // reset login status
        authService.clearCredentials();
  
        $scope.login = function () {
            $scope.dataLoading = true;
            authService.login($scope.email, $scope.password, function(response) {
                if(response.id) {
                    authService.setCredentials($scope.email, $scope.password, response.id, response.roles);
                    $location.path('/dictates');
                } else {
                    $scope.error = response.message;
                    $scope.dataLoading = false;
                }
            });
        };
    });
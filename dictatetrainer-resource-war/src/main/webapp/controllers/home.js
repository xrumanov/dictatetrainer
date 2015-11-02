angular.module('DictateTrainer')
  .controller('HomeCtrl', function($scope, $rootScope, $location, authService) {

        authService.clearCredentials();
    
            $scope.login = function () {
            $scope.dataLoading = true;
            authService.login($scope.email, $scope.password, function(response) {
                if(response.success) {
                    authService.setCredentials($scope.email, $scope.password);
                    //$location.path('/'); go to page where you are logged in
                } else {
                    $scope.error = response.message;
                    $scope.dataLoading = false;
                }
            });
        };
  });
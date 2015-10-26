angular.module('Dictatetrainer')
  .controller('HomeCtrl', function($scope, $window, $rootScope, $auth) {
        $scope.credentials = {};
        
    if ($auth.isAuthenticated() && ($rootScope.currentUser && $rootScope.currentUser.username)) {
    }

    $scope.isAuthenticated = function() {
      return $auth.isAuthenticated();
    };
    $scope.emailLogin = function() {

      $auth.login({ email: $scope.credentials.email, password: $scope.credentials.password })
        .then(function(response) {
          $window.localStorage.currentUser = JSON.stringify(response.data.user);
          $rootScope.currentUser = JSON.parse($window.localStorage.currentUser);
        })
        .catch(function(response) {
          $scope.errorMessage = {};
          angular.forEach(response.data.message, function(message, field) {
            $scope.loginForm[field].$setValidity('server', false);
            $scope.errorMessage[field] = response.data.message[field];
          });
        });
    };
    
    $scope.authenticate = function(provider) {
      $auth.authenticate(provider)        
    };
  });
angular.module('DictateTrainer')
  .controller('SignupCtrl', function($scope, $window, $rootscope) {
    $scope.reg = {};
    
    $scope.signup = function() {
        var user = {
                 name: $scope.reg.name,
                 email: $scope.reg.email,
                 password: $scope.reg.password,
                 type: $scope.reg.usertype
                };

      //$auth.signup(user)
      //  .then(function(response) {
      //    $window.localStorage.currentUser = JSON.stringify(response.data.user);
      //    $rootScope.currentUser = JSON.parse($window.localStorage.currentUser);
      //  })
      //  .catch(function(response) {
      //    console.log(response.data);
      //  });
    };

  });
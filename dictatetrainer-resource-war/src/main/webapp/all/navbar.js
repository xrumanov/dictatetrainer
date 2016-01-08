angular.module('DictateTrainer')
  .controller('NavbarCtrl', function($scope, $rootScope) {

    $scope.name = $rootScope.globals.currentUser.name;

  });
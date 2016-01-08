angular.module('DictateTrainer')
    .controller('LoginCtrl', function ($scope, $rootScope, $location, authService, $auth, loginService) {
        // reset login status
        authService.clearCredentials();

        $scope.login = function () {
            $scope.dataLoading = true;

            loginService.save({email: $scope.email, password: $scope.password})
                .$promise.then(function (response) {

                    authService.setCredentials($scope.email, $scope.password, response.id, response.roles, response.name);
                    $location.path('/');

                }, function (response) {

                    $scope.dataLoading = false;
                    $scope.error = "Vložili jste špatné jméno anebo heslo, zkuste to znovu!";

                })
        };

        $scope.authenticate = function (provider) {
            $auth.authenticate(provider);//.then(function(response) {
            //    $scope.name = response.name;
            //    if()
            //    if(typeof (response.email) != 'undefined'){
            //        $location.path("/student-email");
            //    } else {
            //        $location.path("/student-noemail");
            //    }
            //}, function() {
            //
            //});
        };
    });
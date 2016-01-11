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
                    $scope.error = "Vložili jste špatný email nebo heslo, zkuste to znovu!";

                })
        };

        $scope.authenticate = function (provider) {

            $auth.authenticate(provider).then(function(response){
                $scope.name = response.name; // name from facebook
                    if(typeof (response.email) != ""){
                        $scope.email = response.email;
                        $location.path("#/student-social");
                    } else {
                        $location.path("#/student-social");
                    }
            },function() {
                $scope.error = "Přihlášení skrz sociální síť se nezdařilo, zkuste email.";
            });
        };

        $scope.signup = function() {
            $location.path("#/signup");
        }
    });


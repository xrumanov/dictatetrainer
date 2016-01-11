angular.module('DictateTrainer')
    .controller('ProfileCtrl', function ($scope, $rootScope, userService, $http) {

        $scope.user = {
            id: $rootScope.globals.currentUser.id,
            name: $rootScope.globals.currentUser.name,
            email: $rootScope.globals.currentUser.email,
            type: $rootScope.globals.currentUser.roles[0]
        };


        $scope.updateUser = function () {
            if ($scope.user.type == "STUDENT") {

                // if user is student, add schoolclass property
                userService.get({id: $scope.user.id}).$promise.then(function (response) {
                    //on success getting student, updating
                    $scope.success = "Podařilo se získat data o studentovi, provádím aktualizaci dat.";
                    $scope.error = "";

                    $http.put("/api/users/"+$scope.user.id,{
                        name: $scope.user.name,
                        email: $scope.user.email,
                        type: $scope.user.type,
                        schoolClassId: response.schoolClass.id
                    }).success( function() {
                            // on success updating student
                            $scope.success = "Profil byl úspěšně aktualizován.";
                            $scope.error = "";
                        }).error( function() {
                            // on error updating student
                            $scope.success = "";
                            $scope.error = "Chyba:" + response.errorIdentification + response.errorDescription;
                        })
                }, function (response) {
                    //on error accessing student data write an error
                    $scope.success = "";
                    $scope.error = "Chyba:" + response.errorIdentification + response.errorDescription;
                });
            } else {
                // if user is teacher
                $http.put("/api/users/"+$scope.user.id,{
                    name: $scope.user.name,
                    email: $scope.user.email,
                    type: $scope.user.type
                }).success(function () {
                        // on success in updating data of teacher or admin
                        $scope.success = "Profil byl úspěšně aktualizován.";
                        $scope.error = "";
                    }).error(function (response) {
                        //on error accessing teacher data write an error
                        $scope.success = "";
                        $scope.error = "Chyba:" + response.errorDescription;
                    })
            }
        }
    });
angular.module('DictateTrainer')
    .controller('SignupCtrl', function ($scope, schoolService, userService, schoolClassService, authService, $timeout, $location) {

        // reset login status
        authService.clearCredentials();

        $scope.selectedSchool = {};
        $scope.schools = {};
        $scope.schoolClasses = {};
        $scope.student = {};
        $scope.selectedSchoolClass = {};


        $scope.getSchools = function () {
            schoolService.get().$promise.then(function (schoolResponse) {
                $scope.schools = schoolResponse.entries;
            }, function () {
                $scope.error = "Nebylo možné načítat školy ze seznamu.";
            })
        };

        $scope.getSchoolClasses = function () {
            schoolClassService.get({schoolId: $scope.selectedSchool.id}).$promise.then(function (response) {
                // on success getting schoolclasses
                $scope.schoolClasses = response.entries;
            }, function (response) {
                $scope.error = response.mistake.errorIdentification + " " + errorDescription;
            })
        };

        $scope.createStudent = function () {

            userService.save({
                name: $scope.student.name,
                email: $scope.student.email,
                password: $scope.student.password,
                type: "STUDENT",
                schoolClassId: $scope.selectedSchoolClass.id
            }).$promise.then(function (response) {

                    // on success creating user
                    $scope.success = "Uživatel úspěšně vytvořen. Za okamžik budete přesměrováni zpět na přihlášení.";
                    $scope.error = "";

                    // redirecting to home after 2s
                    $timeout(function(){
                        $location.path('/');
                    }, 2000);

                        }, function (response) {
                    //on failure creating student
                    $scope.success = "";
                    $scope.error = response.errorIdentification + " " + response.errorDescription;
                });

        }
    }
);
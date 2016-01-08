angular.module('DictateTrainer')
    .controller('CreateTeacherCtrl', function ($scope, userService, schoolClassService, schoolService, $location) {

        $scope.teacher = {};
        $scope.schoolClass = {};
        $scope.schools = {};
        $scope.passwordConfirm = "";
        $scope.selectedSchool = {};

        $scope.submitTeacher = function () {

            // are passwords the same?
            if ($scope.teacher.password == $scope.passwordConfirm) {

                userService.save({
                    name: $scope.teacher.name, email: $scope.teacher.email,
                    password: $scope.teacher.password, type: "TEACHER"
                }).$promise.then(function (response) {
                        // onSuccess
                        $scope.success = "Učitel úspěšně vytvořený.";
                        // create school class
                        schoolClassService.save({
                            name: $scope.schoolClass.name,
                            schoolId: $scope.selectedSchool.id,
                            teacherId: response.id
                        }).$promise.then(function () {
                                // on schoolClassCreation success
                                $scope.success = "Učitel a třída úspěšně vytvořená.";
                                $scope.error = "";
                                $scope.teacher = {};
                                $scope.selectedSchool = {};

                            }, function (response) {
                                // on schoolClassCreation failure
                                $scope.success = "";
                                $scope.error = response.data.errorDescription;
                            })

                    }, function (response) {
                        //onTeacherCreation failure
                        $scope.error = response.data.errorDescription;
                    })
            } else {
                $scope.error = "Hesla se nezhodují";
            }
        };

        $scope.getSchools = function () {
            schoolService.get().$promise.then(function (schoolResponse) {
                $scope.schools = schoolResponse.entries;
            }, function () {
                $scope.error = "Nebylo možné načítat školy ze seznamu.";
            })
        }

    });
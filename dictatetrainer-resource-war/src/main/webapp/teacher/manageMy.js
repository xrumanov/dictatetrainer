angular.module('DictateTrainer')
    .controller('MyManagementCtrl', function ($scope, $location, $rootScope,
                                              schoolClassService, userService, dictateService) {

        // get id of logged teacher
        var teacherId = $rootScope.globals.currentUser.id;
        var schoolClassId = -1;

        // get schoolClass of teacher
        $scope.schoolClassesReq = schoolClassService.get({teacherId: teacherId})
            .$promise.then(function (schoolClassResponse) {
                schoolClassId = schoolClassResponse.entries[0].id;

                // get students from schoolClass
                $scope.studentsReq = userService.get({schoolClassId: schoolClassId})
                    .$promise.then(function (studentResponse) {
                        $scope.students = studentResponse.entries;
                    }
                );
            });


        // get dictates from teacher
        $scope.dictatesReq = dictateService.get({uploaderId: teacherId})
            .$promise.then(function (dictateResponse) {
                $scope.teacherDictates = dictateResponse.entries;
            }
        );


        //functions for buttons - dictate management
        $scope.manageDictate = function (id) {
            $location.path("/t-edit-d/" + id);
        };

        //functions for buttons - student management
        $scope.manageUser = function (id) {
            $location.path("/t-edit-u/" + id);
        };

        //functions for buttons - student statistics
        $scope.statsUser = function (id) {
            $location.path("/t-stats-u/" + id);
        };
    });
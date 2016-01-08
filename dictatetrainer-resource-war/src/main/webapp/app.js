'use strict';

angular.module('DictateTrainer', [
    'ngRoute',
    'ngCookies',
    'ngResource',
    'satellizer',
    'ngFileUpload',
    'ngAudio'
])

    .config(function ($routeProvider, $authProvider) {

        $routeProvider

            //admin pages
            .when('/home-a', {
                templateUrl: 'admin/recent_activity_trials.html',
                controller: 'RecentActivityCtrl'
            })

            .when('/add-t', {
                templateUrl: 'admin/create_teacher.html',
                controller: 'CreateTeacherCtrl'
            })

            .when('/add-sc', {
                templateUrl: 'admin/create_school.html',
                controller: 'CreateSchoolCtrl'
            })

            .when('/rec-u', {
                templateUrl: 'admin/recent_activity_users.html',
                controller: 'RecentActivityCtrl'
            })

            .when('/rec-d', {
                templateUrl: 'admin/recent_activity_dictates.html',
                controller: 'RecentActivityCtrl'
            })

            .when('/rec-c', {
                templateUrl: 'admin/recent_activity_categories.html',
                controller: 'RecentActivityCtrl'
            })

            .when('/rec-t', {
                templateUrl: 'admin/recent_activity_trials.html',
                controller: 'RecentActivityCtrl'
            })

            .when('/rec-s', {
                templateUrl: 'admin/recent_activity_schools.html',
                controller: 'RecentActivityCtrl'
            })

            .when('/rec-sc', {
                templateUrl: 'admin/recent_activity_school_classes.html',
                controller: 'RecentActivityCtrl'
            })

            .when('/mng-u/', {
                templateUrl: 'admin/manage_users.html',
                controller: 'ManagementCtrl'
            })

            .when('/edit-u/:id', {
                templateUrl: 'admin/edit_user.html',
                controller: 'EditUserCtrl'
            })

            .when('/mng-d', {
                templateUrl: 'admin/manage_dictates.html',
                controller: 'ManagementCtrl'
            })

            .when('/edit-d/:id', {
                templateUrl: 'admin/edit_dictate.html',
                controller: 'EditDictateCtrl'
            })

            .when('/profile-a', {
                templateUrl: 'admin/profile.html',
                controller: 'ProfileCtrl'
            })



            //student pages
            .when('/home-s', {
                templateUrl: 'student/dictates.html',
                controller: 'DictatesCtrl'
            })

            .when('/list-d', {
                templateUrl: 'student/dictates.html',
                controller: 'DictatesCtrl'
            })

            .when('/details-e', {
                templateUrl: 'student/error_details.html'
            })

            .when('/stats-date', {
                templateUrl: 'student/statistics_student_date.html',
                controller: 'StatisticsCtrl'
            })

            .when('/stats-dict', {
                templateUrl: 'student/statistics_student_dictate.html',
                controller: 'StatisticsCtrl'
            })

            .when('/stats-t', {
                templateUrl: 'student/statistics_student_trials.html',
                controller: 'StatisticsCtrl'
            })

            .when('/train', {
                templateUrl: 'student/dictates.html',
                controller: 'DictatesCtrl'
            })

            .when('/trainer/:filename', {
                templateUrl: 'student/trainer.html',
                controller: 'TrainerCtrl'
            })

            .when('/profile-s', {
                templateUrl: 'student/profile.html',
                controller: 'ProfileCtrl'
            })

            .when('/results-sum', {
                templateUrl: 'student/result_trial_summary.html',
                controller: 'ResultCtrl'
            })

            .when('/results-details', {
                templateUrl: 'student/result_trial_errors.html',
                controller: 'ResultCtrl'
            })

            .when('/results-txt', {
                templateUrl: 'student/result_trial_text.html',
                controller: 'ResultCtrl'
            })

            .when('/error-details/:id', {
                templateUrl: 'student/error_details.html',
                controller: 'ErrorDetailsCtrl'
            })

            .when('/teacher-results-sum', {
                templateUrl: 'student/result_trial_summary.html',
                controller: 'ResultCtrl'
            })

            .when('/teacher-results-details', {
                templateUrl: 'student/result_trial_errors.html',
                controller: 'ResultCtrl'
            })

            .when('/teacher-results-txt', {
                templateUrl: 'student/result_trial_text.html',
                controller: 'ResultCtrl'
            })




            //teacher pages
            .when('/home-t', {
                templateUrl: 'teacher/manage_my_dictates.html',
                controller: 'MyManagementCtrl'
            })

            .when('/details-d', {
                templateUrl: 'teacher/dictate_details.html'
            })

            .when('/edit-s', {
                templateUrl: 'teacher/edit_student.html'
            })

            .when('/mng-my-students', {
                templateUrl: 'teacher/manage_my_students.html',
                controller: 'MyManagementCtrl'
            })

            .when('/my-dictates', {
                templateUrl: 'teacher/manage_my_dictates.html',
                controller: 'MyManagementCtrl'
            })

            .when('/stats-dictate', {
                templateUrl: 'teacher/statistics_dictate.html'
            })

            .when('/stats-students', {
                templateUrl: 'teacher/statistics_student.html'
            })

            .when('/t-edit-u/:id', {
                templateUrl: 'teacher/edit_student.html',
                controller: 'EditUserCtrl'
            })

            .when('/my-dictates', {
                templateUrl: 'teacher/manage_my_dictates.html',
                controller: 'MyManagementCtrl'
            })

            .when('/t-edit-d/:id', {
                templateUrl: 'teacher/edit_dictate.html',
                controller: 'EditDictateCtrl'
            })

            .when('/upload', {
                templateUrl: 'teacher/upload.html',
                controller: 'UploadCtrl'
            })

            .when('/profile-t', {
                templateUrl: 'teacher/profile.html',
                controller: 'ProfileCtrl'
            })




            //pages for all
            .when('/login', {
                templateUrl: 'all/login.html',
                controller: 'LoginCtrl'
            })

            .when('/signup', {
                templateUrl: 'all/signup.html',
                controller: 'SignupCtrl'
            })

            .otherwise({redirectTo: '/login'}); //404


        $authProvider.facebook({
            clientId: '733955933328704',
            url: '/api/users/authenticate/facebook',
            scope: ['email'],
            scopeDelimiter: ',',
            requiredUrlParams: ['display', 'scope'],
            authorizationEndpoint: 'https://www.facebook.com/v2.5/dialog/oauth'
        });

        $authProvider.google({
            clientId: '990818100364-os7jh9mbcujlq3edhl2jitre64gv8t67.apps.googleusercontent.com'
        });

    })

    .run(['$rootScope', '$location', '$cookieStore', '$http',
        function ($rootScope, $location, $cookieStore, $http) {
            // keep user logged in after page refresh
            $rootScope.globals = $cookieStore.get('globals') || {};
            if ($rootScope.globals.currentUser) {
                $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
            }

            $rootScope.$on('$locationChangeStart', function (event, next, current) {
                // redirect to login page if not logged in
                if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
                    $location.path('/login');
                }

                if ($location.path() == '/' && $rootScope.globals.currentUser) {
                    if ($rootScope.globals.currentUser.roles[0] == 'STUDENT') {
                        $location.path('/home-s');
                    } else if ($rootScope.globals.currentUser.roles[0] == 'TEACHER' &&
                        $rootScope.globals.currentUser.roles[1] == 'ADMINISTRATOR') {
                        $location.path('/home-a');
                    } else if ($rootScope.globals.currentUser.roles[0] == 'TEACHER') {
                        $location.path('/home-t');
                    }
                }

                //if (((($location.path() == '/home-a') || ($location.path() == '/home-t')
                //    && $rootScope.globals.currentUser.roles[0] == 'STUDENT')) ||
                //    (($location.path() == '/home-s') || ($location.path() == '/home-t')
                //    && $rootScope.globals.currentUser.roles[0] == 'ADMINISTRATOR') ||
                //    (($location.path() == '/home-a') || ($location.path() == '/home-s')
                //    && $rootScope.globals.currentUser.roles[0] == 'TEACHER')) {
                //    $location.path('/login');
                //}
            });
        }]);
'use strict';

angular.module('DictateTrainer', [
    'ngRoute',
    'ngCookies',
    'ngResource'
])

    .config(function ($routeProvider) {

        $routeProvider
            .when('/', {
                templateUrl: 'views/home.html',
                controller: 'HomeCtrl'
            })

            .when('/login', {
                templateUrl: 'views/login.html',
                controller: 'LoginCtrl'
            })

            .when('/signup', {
                templateUrl: 'views/signup.html',
                controller: 'SignupCtrl'
            })

            .when('/dictates', {
                templateUrl: 'views/dictates.html',
                controller: 'DictatesCtrl',
                access: {
                    requiresLogin: true
                }
            })

            .when('/profile', {
                templateUrl: 'views/profile.html',
                controller: 'ProfileCtrl',
                access: {
                    requiresLogin: true
                }
            })

            .when('/students', {
                templateUrl: 'views/students.html',
                controller: 'StudentsCtrl',
                access: {
                    requiresLogin: true
                }
            })

            .when('/upload', {
                templateUrl: 'views/upload.html',
                controller: 'UploadCtrl',
                access: {
                    requiresLogin: true
                }
            })

            .when('/trainer/:filename', {
                templateUrl: 'views/trainer.html',
                controller: 'TrainerCtrl',
                access: {
                    requiresLogin: true
                }
            })

            .otherwise({redirectTo: '/login'}); //404
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
            });
        }]);
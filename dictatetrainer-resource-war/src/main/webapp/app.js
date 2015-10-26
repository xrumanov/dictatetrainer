angular.module('Dictatetrainer', ['ngRoute', 'ngMessages', 'satellizer'])
  .config(function($routeProvider, $authProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/home.html',
        controller: 'HomeCtrl'
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
      .when('/trainer', {
        templateUrl: 'views/trainer.html',
            controller: 'TrainerCtrl',
        access: {
        requiresLogin: true
        }
      })
      .otherwise('/');

//    comment this out if deploy to server
    $authProvider.loginUrl = '/api/users/authenticate/jwt';
    $authProvider.signupUrl = '/api/users';
    $authProvider.facebook({
      clientId: '733955933328704',
      url: '/api/users/authenticate/facebook'
    });
    
  })
  .run(function($rootScope, $window, $auth) {
    if ($auth.isAuthenticated()) {
      $rootScope.currentUser = JSON.parse($window.localStorage.currentUser);
    }
  });

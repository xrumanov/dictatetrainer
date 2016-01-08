angular.module('DictateTrainer')

    .factory('categoryService', function ($resource) {
        return $resource('/api/categories/:id');
    })

    .factory('dictateService', function ($resource) {
        return $resource('/api/dictates/:id');
    })

    .factory('userService', function ($resource) {
        return $resource('/api/users/:id');
    })

    .factory('errorService', function ($resource) {
        return $resource('/api/errors/:id');
    })

    .factory('trialService', function ($resource) {
        return $resource('/api/trials/:id');
    })

    .factory('correctingService', function ($resource) {
        return $resource('/api/correctDictate');
    })

    .factory('schoolService', function ($resource) {
        return $resource('/api/schools');
    })

    .factory('schoolClassService', function ($resource) {
        return $resource('/api/schoolclasses');
    })

    .factory('loginService', function ($resource) {
        return $resource('/api/users/authenticate');
    });
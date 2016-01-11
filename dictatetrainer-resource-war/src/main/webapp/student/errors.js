angular.module('DictateTrainer')
    .controller('ErrorsCtrl', function ($scope, $location, $rootScope,
                                        errorService, dictateService, $routeParams) {
        var idParam = $routeParams.id;
        var groupfield = $routeParams.groupfield;
        var studentId = $rootScope.globals.currentUser.id;

        $scope.errorsToShow = {};
        $scope.dictate = {};
        $scope.numOfMistakes = -1;
        $scope.uniqueMistakes = -1;
        $scope.mostOcurrencesType = "";


        $scope.onInit = function () {


            if (groupfield === 'dictates') {

                //get dictate
                dictateService.get({id: idParam}).$promise.then(function (response) {
                    // on get dictate success
                    $scope.dictate = response;

                }, function (response) {
                    // on get dictate failure
                    $scope.error = response.errorIdentification + " " + errorDescription;
                });


                //get mistakes
                errorService.get({studentId: studentId, dictateId: idParam}).$promise.then(function (response) {
                    // on mistake get success
                    $scope.errorsToShow = response.entries;
                    $scope.numOfMistakes = response.entries.length;

                    //create set of unique word positions in dictate
                    var um = new Set([]);
                    var ta = {};

                    //extract it from the response
                    for (var i = 0; i < response.entries.length; i++) {
                        um.add(response.entries[i].wordPosition);
                        ta[i] = response.entries[i].errorType;
                    }

                    $scope.uniqueMistakes = um.size;

                    $scope.mostOccurrencesType = mostOccurences(ta);


                }, function (response) {
                    // on error failure
                    $scope.error = response.errorIdentification + " " + errorDescription;
                });


            } else if(groupfield === 'trials') {
                //get mistakes
                errorService.get({studentId: studentId, trialId: idParam}).$promise.then(function (response) {
                    // on mistake get success
                    $scope.errorsToShow = response.entries;
                    $scope.numOfMistakes = response.entries.length;

                    //create set of unique word positions in dictate
                    var um = new Set([]);
                    var ta = {};

                    //extract it from the response
                    for (var i = 0; i < response.entries.length; i++) {
                        um.add(response.entries[i].wordPosition);
                        ta[i] = response.entries[i].errorType;
                    }

                    $scope.uniqueMistakes = um.size;

                    $scope.mostOccurrencesType = mostOccurences(ta);


                }, function (response) {
                    // on error failure
                    $scope.error = response.errorIdentification + " " + errorDescription;
                })
            }
        };


        $scope.errorDetails = function (id) {
            $location.path('/error-details/' + id);
        };

        /**
         * get most occurences of an array
         * @param array
         * @returns {*}
         */
        function mostOccurences(array) {
            if (array.length == 0)
                return null;
            var modeMap = {};
            var maxEl = array[0], maxCount = 1;
            for (var i = 0; i < array.length; i++) {
                var el = array[i];
                if (modeMap[el] == null)
                    modeMap[el] = 1;
                else
                    modeMap[el]++;
                if (modeMap[el] > maxCount) {
                    maxEl = el;
                    maxCount = modeMap[el];
                }
            }
            return maxEl;
        }
    });
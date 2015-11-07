angular.module('DictateTrainer')
    .controller('TrainerCtrl', function ($scope, $rootScope, $route, $routeParams, $sce,
                                         dictateService, correctingService) {

        $scope.userText = {};
        var transcript = "";

        var filenameParam = $routeParams.filename;

        //get the dictate by given filename
        $scope.dictate = dictateService.get({filename: filenameParam});
        $scope.dictate.$promise.then(function (data) {
            $scope.pathToDictate = "/dictate/" + data.entries[0].filename;
            transcript = data.entries[0].transcript;
        });


        //take user input and transcript from database and correct the dictate
        $scope.correctDictate = function () {
            correctingService.save({
                dictateTranscript: transcript,
                userText: $scope.userText.val
            });
        }
    })


    //trust only resources given to this filter
    .filter("trustUrl", ['$sce', function ($sce) {
        return function (recordingUrl) {
            return $sce.trustAsResourceUrl(recordingUrl);
        };
    }]);
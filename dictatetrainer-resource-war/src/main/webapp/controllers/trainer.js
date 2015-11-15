angular.module('DictateTrainer')
    .controller('TrainerCtrl', function ($scope, $rootScope, $route, $routeParams, $sce,
                                         dictateService, correctingService, errorService, trialService, $http) {

        $scope.userText = {};
        var transcript = "";
        var dictateId = -1;
        var filenameParam = $routeParams.filename;

        //get the dictate by given filename
        $scope.dictate = dictateService.get({filename: filenameParam});
        $scope.dictate.$promise.then(function (data) {
            $scope.pathToDictate = "/dictate/" + data.entries[0].filename;
            transcript = data.entries[0].transcript;
            dictateId = data.entries[0].id;
        });


        //take user input from UI and transcript from database and correct the dictate
        $scope.correctDictate = function () {

            var mistakeArray = {};
            var numberOfMistakes = 0;
            var trialId = -1;
            var studentId = $rootScope.globals.currentUser.id;

            $scope.corrector = correctingService.save({dictateTranscript: transcript, userText: $scope.userText.val});
            //return response consisting of mistakes
            $scope.corrector.$promise.then(function (mistakesResponseData) {
                numberOfMistakes = mistakesResponseData.totalMistakes;
                mistakeArray = mistakesResponseData.mistakes;

                //add test if there was success in all error storing
                $scope.trial = trialService.save({
                    trialText: $scope.userText.val,
                    studentId: studentId,
                    dictateId: dictateId
                });

                $scope.trial.$promise.then(function (trialResponseData) {

                    trialId = trialResponseData.id;
                    //get response from correcting service and save it to db as errors in a loop
                    for (i = 0; i < numberOfMistakes; i++) {
                        $scope.error = errorService.save({
                            //store error in db
                            mistakeCharPosInWord: mistakeArray[i].mistakeCharPosInWord,
                            correctChars: mistakeArray[i].correctChars,
                            writtenChars: mistakeArray[i].writtenChars,
                            correctWord: mistakeArray[i].correctWord,
                            writtenWord: mistakeArray[i].writtenWord,
                            wordPosition: mistakeArray[i].wordPosition,
                            lemma: mistakeArray[i].lemma,
                            posTag: mistakeArray[i].posTag,
                            sentence: mistakeArray[i].sentence,
                            errorPriority: mistakeArray[i].priority,
                            errorDescription: mistakeArray[i].mistakeDescription,
                            dictateId: dictateId,
                            studentId: studentId,
                            trialId: trialId
                        });
                    }
                });
            });

        }
    })


    //trust only resources given to this filter
    .filter("trustUrl", ['$sce', function ($sce) {
        return function (recordingUrl) {
            return $sce.trustAsResourceUrl(recordingUrl);
        };
    }]);
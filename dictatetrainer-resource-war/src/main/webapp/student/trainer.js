angular.module('DictateTrainer')
    .controller('TrainerCtrl', function ($scope, $rootScope, $route, $routeParams, $sce,
                                         dictateService, correctingService, errorService, trialService, ngAudio, $location) {

        $scope.userText = {};
        $scope.isDisabled = false;
        $scope.showCorr = false;
        var transcript = "";
        var dictateId = -1;
        var defaultRepetitionForDictate = 0;
        var filenameParam = $routeParams.filename;


        //get the dictate by given filename
        $scope.dictate = dictateService.get({filename: filenameParam});
        $scope.dictate.$promise.then(function (data) {
            $scope.pathToDictate = "/dictate/" + data.entries[0].filename;
            transcript = data.entries[0].transcript;
            dictateId = data.entries[0].id;
            defaultRepetitionForDictate = data.entries[0].defaultRepetitionForDictate;


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

                //for future use with displaying the results
                $rootScope.mistakeArray = mistakeArray;
                $rootScope.userText = transcript;

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
                            previousWord: mistakeArray[i].previousWord,
                            nextWord: mistakeArray[i].nextWord,
                            wordPosition: mistakeArray[i].wordPosition,
                            lemma: mistakeArray[i].lemma,
                            posTag: mistakeArray[i].posTag,
                            sentence: mistakeArray[i].sentence,
                            errorPriority: mistakeArray[i].priority,
                            errorDescription: mistakeArray[i].mistakeDescription,
                            errorType: mistakeArray[i].mistakeType,
                            dictateId: dictateId,
                            studentId: studentId,
                            trialId: trialId
                        });
                    }
                });
            });
            $location.path("/results-sum");
        };

        $scope.test = function() {
            $location.path("/results-sum");
        };

        $scope.playAudio = function() {
            $scope.audio = ngAudio.load("/dictate/" + filenameParam).play();
            $scope.audio.loop = defaultRepetitionForDictate-1;
            if($scope.audio.loop == 0) {
                $scope.showCorr = true;
            }
            $scope.isDisabled = true;
        };

    });
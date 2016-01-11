angular.module('DictateTrainer')
    .controller('ResultCtrl', function ($scope, $rootScope, errorService, $routeParams, trialService) {

        var trialId = $routeParams.trialId;


        $scope.errorArray = {};
        $scope.userText = {};
        //$scope.tokens = {};
        $scope.numOfMistakes = 0;

        $scope.initialize = function () {

            errorService.get({trialId: trialId}).$promise.then(function (response) {
                // when get is successful
                $scope.errorArray = response.entries;
                $scope.numOfMistakes = response.entries.length;


                trialService.get({id: response.entries[0].trial.id}).$promise.then(function (response) {
                    // when get is successful
                    $scope.userText = response.trialText;
                }, function(response){
                    // when error
                    $scope.error = response.errorIdentification + " " + errorDescription;
                });

                //// building the whole text and annotating mistakes
                //var text = $scope.userText;
                //var tokens = text.split(" ");
                //
                //// annotating the mistakes in text (only when there is not whole word missing)
                //for (var i = 0; i < $scope.mistakeArray.length; i++) {
                //    if ($scope.mistakeArray[i].mistakeCharPosInWord > 0) {
                //        var help = tokens[$scope.mistakeArray[i].mistakeCharPosInWord - 1];
                //        tokens[$scope.mistakeArray[i].mistakeCharPosInWord] = "\<font color=\"red\"\>" + help + "\<\/font\>";
                //    }
                //}
                //
                //for (var j = 0; j < tokens.length; j++) {
                //    $scope.userText += tokens[j];
                //    $scope.userText += " ";
                //}

            }, function (response) {
                $scope.error = response.errorIdentification + " " + errorDescription;
            });
        }
    });
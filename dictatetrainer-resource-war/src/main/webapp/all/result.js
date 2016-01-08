angular.module('DictateTrainer')
    .controller('ResultCtrl', function ($scope, $rootScope) {

        $scope.arrayUndefined = false;
        $scope.arrayEmpty = false;
        $scope.mistakeArray = {};
        $scope.userText = {};
        $scope.tokens = {};
        $scope.numOfMistakes = 0;

        $scope.initialize = function() {
            //if (typeof($rootScope.mistakeArray) == 'undefined' || $rootScope.userText == 'undefined') {
            //    $scope.arrayUndefined = true;
            //} else if ($rootScope.mistakeArray.length == null || $rootScope.userText == null) {
            //    $scope.arrayUndefined = false;
            //    $scope.arrayEmpty = true;
            //} else {
            $rootScope.$watch('mistakeArray', 'userText', function(){
                $scope.mistakeArray = $rootScope.mistakeArray;
                $scope.numOfMistakes = $rootScope.mistakeArray.length;
                $scope.tokens = $rootScope.userText.split(' ');

                // annotating the mistakes in text (only when there is not whole word missing)
                for (var i = 0; i < $scope.mistakeArray.length; i++) {
                    if ($scope.mistakeArray[i].mistakeCharPosInWord > 0) {
                        var help = $scope.tokens[$scope.mistakeArray[i].mistakeCharPosInWord - 1];
                        $scope.tokens[$scope.mistakeArray[i].mistakeCharPosInWord] = "\<font color=\"red\"\>" + help + "\<\/font\>";
                    }
                }

                for (var j = 0; j < $scope.tokens.length; j++) {
                    $scope.userText += $scope.tokens[j];
                    $scope.userText += " ";
                }
            });


            };
        //}
    });
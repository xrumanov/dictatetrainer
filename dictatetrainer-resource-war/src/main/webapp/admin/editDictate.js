angular.module('DictateTrainer')
    .controller('EditDictateCtrl', function ($scope, $http, $routeParams) {

        var idParam = $routeParams.id;

        $scope.categories = {};
        $scope.dictateToEdit = {};
        $scope.chosenCategory = {};

        $scope.onInit = function () {

            //get dictate to edit
            $http.get("/api/dictates/" + idParam)
                .success(function (response) {
                    $scope.dictateToEdit = response;

                }).error(function (response) {
                    $scope.success = "";
                    $scope.error = "Chyba: " + response.errorDescription;
                });

            //get categories for dictate to choose
            $http.get("/api/categories")
                .success(function (response) {
                    $scope.categories = response.entries;
                }).error(function (response) {
                    $scope.success = "";
                    $scope.error = "Chyba: " + response.errorDescription;
                });
        };

        $scope.updateDictate = function (id) {
            $http.put("/api/dictates/" + id, {
                name: $scope.dictateToEdit.name,
                description: $scope.dictateToEdit.description,
                filename: $scope.dictateToEdit.filename,
                transcript: $scope.dictateToEdit.transcript,
                defaultRepetitionForDictate: $scope.dictateToEdit.defaultRepetitionForDictate,
                defaultRepetitionForSentences: $scope.dictateToEdit.defaultRepetitionForSentences,
                defaultPauseBetweenSentences: $scope.dictateToEdit.defaultPauseBetweenSentences,
                sentenceEndings: $scope.dictateToEdit.sentenceEndings,
                categoryId: $scope.chosenCategory.id,
                uploaderId: $scope.dictateToEdit.uploader.id
            })
                .success(function (response) {
                    $scope.updatedDictate = response;
                    $scope.success = "Diktát úspěšně aktualizován.";
                    $scope.error = "";
                }).error(function (response) {
                    $scope.success = "";
                    $scope.error = "Chyba: " + response.errorDescription;
                });
        }
    });
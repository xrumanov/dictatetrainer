angular.module('DictateTrainer')
    .controller('UploadCtrl', function ($scope, $http, Upload, $timeout, dictateService) {

        $scope.files = {};

        $scope.$watch('files', function () {
            $scope.upload($scope.files);
        });
        $scope.$watch('file', function () {
            if ($scope.file != null) {
                $scope.files = [$scope.file];

            }
        });

        $scope.log = '';
        $scope.pauses = '';

        $scope.onInit = function () {
            $http.get("/api/categories")
                .success(function (response) {
                    $scope.categories = response.entries;
                });
        };

        $scope.uploadDictate = function() {

            dictateService.save({
                name: $scope.dictate.name,
                description: $scope.dictate.description,
                filename: $scope.files[0].name,
                transcript: $scope.dictate.transcript,
                defaultRepetitionForDictate: $scope.dictate.defaultRepetitionForDictate,
                defaultRepetitionForSentences: 1,
                defaultPauseBetweenSentences: 0,
                sentenceEndings: 0,
                categoryId: $scope.chosenCategory.id,
                uploaderId: $scope.uploaderId
            })
                .$promise.then(function (response) {
                    $scope.updatedDictate = response;
                    $scope.success = "Diktát úspěšně aktualizován.";
                    $scope.error = "";
                },function (response) {
                    $scope.success = "";
                    $scope.error = "Chyba: " + response.errorDescription;
                });
        };



        $scope.upload = function (files) {
            if (files && files.length) {
                for (var i = 0; i < files.length; i++) {
                    var file = files[i];
                    if (!file.$error) {
                        Upload.upload({
                            url: '/api/upload',
                            data: {
                                file: file
                            }
                        }).progress(function (evt) {
                            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                            $scope.log = 'progress: ' + progressPercentage + '% ' +
                                evt.config.data.file.name + '\n' + $scope.log;
                        }).success(function (data, status, headers, config) {
                            $timeout(function () {
                                $scope.log = 'file: ' + config.data.file.name + ', Response: ' + JSON.stringify(data) +
                                    '\n' + $scope.log;
                                //$scope.pauses = output from wavesurfer extractregions function
                            });
                        });
                    }
                }
            }
        };

    });
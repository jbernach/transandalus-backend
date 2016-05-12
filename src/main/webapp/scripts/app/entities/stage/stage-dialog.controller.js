'use strict';

angular.module('backendApp').controller('StageDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Stage', 'Province', '$translate',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Stage, Province, $translate) {

        $scope.language = $translate.use();
        
        $scope.stage = entity;
        $scope.provinces = Province.query({size:1000});
        $scope.stages = Stage.query({size:1000});
        $scope.load = function(id) {
            Stage.get({id : id}, function(result) {
                $scope.stage = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('backendApp:stageUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.stage.id != null) {
                Stage.update($scope.stage, onSaveSuccess, onSaveError);
            } else {
                Stage.save($scope.stage, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;

        $scope.clearTrack = function(stage){
            stage.track.content = null;
            stage.track.contentType = null;
        };

        $scope.setTrack= function ($file, stage) {
            if ($file && $file.$error == 'pattern') {
                return;
            }
            if ($file) {
                var fileReader = new FileReader();
                fileReader.readAsText($file);
                fileReader.onload = function (e) {
                    var textData = e.target.result;
                    $scope.$apply(function() {
                        if(stage.track == null){
                            stage.track = {};
                        }
                        stage.track.content = textData;
                        stage.track.contentType = $file.type;
                    });
                };
            }
        };
}]);

'use strict';

angular.module('backendApp').controller('StageDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Stage', 'Province',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Stage, Province) {

        $scope.stage = entity;
        $scope.provinces = Province.query();
        $scope.stages = Stage.query();
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

        $scope.clearImage = function(stage){
            stage.image.content = '';
            stage.image.contentType = '';
        };

        $scope.setImage = function ($file, stage) {
            if ($file && $file.$error == 'pattern') {
                return;
            }
            if ($file) {
                var fileReader = new FileReader();
                fileReader.readAsDataURL($file);
                fileReader.onload = function (e) {
                    var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        if(stage.image == null){
                            stage.image = {};
                        }
                        stage.image.content = base64Data;
                        stage.image.contentType = $file.type;
                    });
                };
            }
        };
}]);

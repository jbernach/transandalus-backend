'use strict';

angular.module('backendApp').controller('ProvinceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Province',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Province) {

        $scope.province = entity;
        $scope.load = function(id) {
            Province.get({id : id}, function(result) {
                $scope.province = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('backendApp:provinceUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.province.id != null) {
                Province.update($scope.province, onSaveSuccess, onSaveError);
            } else {
                Province.save($scope.province, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;

        $scope.clearImage = function(province){
            province.image.content = '';
            province.image.contentType = '';
        };

        $scope.setImage = function ($file, province) {
            if ($file && $file.$error == 'pattern') {
                return;
            }
            if ($file) {
                var fileReader = new FileReader();
                fileReader.readAsDataURL($file);
                fileReader.onload = function (e) {
                    var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        if(province.image == null){
                            province.image = {};
                        }
                        province.image.content = base64Data;
                        province.image.contentType = $file.type;
                    });
                };
            }
        };
}]);

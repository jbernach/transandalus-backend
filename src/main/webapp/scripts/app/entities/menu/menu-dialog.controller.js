'use strict';

angular.module('backendApp').controller('MenuDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Menu',
        function($scope, $stateParams, $uibModalInstance, entity, Menu) {

        $scope.menu = entity;
        $scope.load = function(id) {
            Menu.get({id : id}, function(result) {
                $scope.menu = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('backendApp:menuUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.menu.id != null) {
                Menu.update($scope.menu, onSaveSuccess, onSaveError);
            } else {
                Menu.save($scope.menu, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);

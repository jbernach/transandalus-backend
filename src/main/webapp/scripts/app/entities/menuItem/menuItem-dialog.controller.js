'use strict';

angular.module('backendApp').controller('MenuItemDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'MenuItem', 'Menu', '$translate',
        function($scope, $stateParams, $uibModalInstance, entity, MenuItem, Menu, $translate) {

        $scope.language = $translate.use();
        
        $scope.menuItem = entity;
        $scope.menus = Menu.query({size:1000});
        
        $scope.load = function(id) {
            MenuItem.get({id : id}, function(result) {
                $scope.menuItem = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('backendApp:menuItemUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.menuItem.id != null) {
                MenuItem.update($scope.menuItem, onSaveSuccess, onSaveError);
            } else {
                MenuItem.save($scope.menuItem, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);

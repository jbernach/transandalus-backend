'use strict';

angular.module('backendApp')
	.controller('MenuItemDeleteController', function($scope, $uibModalInstance, entity, MenuItem) {

        $scope.menuItem = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MenuItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });

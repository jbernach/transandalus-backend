'use strict';

angular.module('backendApp')
	.controller('MenuDeleteController', function($scope, $uibModalInstance, entity, Menu) {

        $scope.menu = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Menu.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });

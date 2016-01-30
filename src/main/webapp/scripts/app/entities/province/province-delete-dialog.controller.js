'use strict';

angular.module('backendApp')
	.controller('ProvinceDeleteController', function($scope, $uibModalInstance, entity, Province) {

        $scope.province = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Province.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });

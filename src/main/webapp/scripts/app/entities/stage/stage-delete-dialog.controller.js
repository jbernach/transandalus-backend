'use strict';

angular.module('backendApp')
	.controller('StageDeleteController', function($scope, $uibModalInstance, entity, Stage) {

        $scope.stage = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Stage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });

'use strict';

angular.module('backendApp')
    .controller('StageDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Stage, Province) {
        $scope.stage = entity;
        $scope.load = function (id) {
            Stage.get({id: id}, function(result) {
                $scope.stage = result;
            });
        };
        var unsubscribe = $rootScope.$on('backendApp:stageUpdate', function(event, result) {
            $scope.stage = result;
        });
        $scope.$on('$destroy', unsubscribe);

         $scope.byteSize = DataUtils.byteSize;
    });

'use strict';

angular.module('backendApp')
    .controller('ProvinceDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Province) {
        $scope.province = entity;

        $scope.load = function (id) {
            Province.get({id: id}, function(result) {
                $scope.province = result;
            });
        };
        var unsubscribe = $rootScope.$on('backendApp:provinceUpdate', function(event, result) {
            $scope.province = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.byteSize = DataUtils.byteSize;
    });

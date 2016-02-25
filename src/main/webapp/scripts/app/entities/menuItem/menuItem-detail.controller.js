'use strict';

angular.module('backendApp')
    .controller('MenuItemDetailController', function ($scope, $rootScope, $stateParams, entity, MenuItem, Menu) {
        $scope.menuItem = entity;
        $scope.load = function (id) {
            MenuItem.get({id: id}, function(result) {
                $scope.menuItem = result;
            });
        };
        var unsubscribe = $rootScope.$on('backendApp:menuItemUpdate', function(event, result) {
            $scope.menuItem = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });

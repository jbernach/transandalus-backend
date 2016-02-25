'use strict';

angular.module('backendApp')
    .controller('MenuController', function ($scope, $state, Menu) {

        $scope.menus = [];
        $scope.loadAll = function() {
            Menu.query(function(result) {
               $scope.menus = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.menu = {
                name: null,
                id: null
            };
        };
    });

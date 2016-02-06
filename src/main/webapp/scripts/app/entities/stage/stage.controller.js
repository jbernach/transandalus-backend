'use strict';

angular.module('backendApp')
    .controller('StageController', function ($scope, $state, Stage, ParseLinks) {

        $scope.stages = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Stage.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.stages = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.stage = {
                name: null,
                description: null,
                distanceTotal: null,
                distanceRoad: null,
                estimatedTime: null,
                elevation: null,
                difficultyPhys: null,
                difficultyTech: null,
                galleryURL: null,
                image: {id:null, content: null, contentType: null},
                id: null
            };
        };
    });

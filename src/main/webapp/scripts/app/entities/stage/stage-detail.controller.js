'use strict';

angular.module('backendApp')
    .controller('StageDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Stage, Province, $location) {
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

        $scope.map = {
            //Cordoba
            center: {
              latitude: 37.891581,
              longitude: -4.778564
            },
            zoom:7,
            options: {scrollwheel:false}
        };

         if($scope.stage.track){
            var url = 'http://' + window.location.hostname + ':' + window.location.port + window.location.pathname + 'api/tracks/'+$scope.stage.track.id;
            $scope.map.kmlLayerOptions = {'url':url};
            console.debug(url)
        }
    });

'use strict';

angular.module('backendApp')
    .controller('ProvinceDetailController', function ($scope, $rootScope, $stateParams, DataUtils, entity, Province, $location) {
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

        $scope.map = {
            //Cordoba
            center: {
              latitude: 37.891581,
              longitude: -4.778564
            },
            zoom:7,
            options: {scrollwheel:false}
        };


        if($scope.province.$promise){
            $scope.province.$promise.then(function(el){
                if(el.track){
                    var url = 'http://' + window.location.hostname + ':' + window.location.port + window.location.pathname + 'api/tracks/'+el.track.id;
                    $scope.map.kmlLayerOptions = {'url':url};
                }
          });
        }
         
    });

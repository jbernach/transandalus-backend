'use strict';

angular.module('transaServices')
    .factory('Province', function ($resource) {
        return $resource('api/provinces/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'updateKml': { method:'PUT', url: 'api/provinces/generate_kml/:id'}
        });
    });

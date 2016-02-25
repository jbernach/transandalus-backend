'use strict';

angular.module('backendApp')
    .factory('MenuItem', function ($resource, DateUtils) {
        return $resource('api/menuItems/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

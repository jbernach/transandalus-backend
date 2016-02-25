'use strict';

angular.module('backendApp')
    .factory('Menu', function ($resource, DateUtils) {
        return $resource('api/menus/:id', {}, {
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

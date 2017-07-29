(function() {
    'use strict';

    angular
        .module('transaServices')
        .factory('Volunteer', Volunteer);

    Volunteer.$inject = ['$resource'];

    function Volunteer ($resource) {
        return $resource('api/volunteers/:id', {}, {
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
    }
})();

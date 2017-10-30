(function() {
    'use strict';

    angular
        .module('transaServices')
        .factory('Traveler', Traveler);

    Traveler.$inject = ['$resource'];

    function Traveler ($resource) {
        return $resource('api/travelers/:id', {}, {
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

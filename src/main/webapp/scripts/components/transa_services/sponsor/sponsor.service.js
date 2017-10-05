(function() {
    'use strict';

    angular
        .module('transaServices')
        .factory('Sponsor', Sponsor);

    Sponsor.$inject = ['$resource'];

    function Sponsor ($resource) {
        return $resource('api/sponsors/:id', {}, {
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

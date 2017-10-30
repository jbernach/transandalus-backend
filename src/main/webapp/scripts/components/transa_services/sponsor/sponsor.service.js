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
                    data.fromDate = new Date(data.fromDate);
                    data.toDate = new Date(data.toDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

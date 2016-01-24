'use strict';

angular.module('backendApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



(function() {
    'use strict';

    angular
        .module('backendApp')
        .controller('TravelerDetailController', TravelerDetailController);

    TravelerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Traveler'];

    function TravelerDetailController($scope, $rootScope, $stateParams, entity, Traveler) {
        var vm = this;
        vm.traveler = entity;
        vm.load = function (id) {
            Traveler.get({id: id}, function(result) {
                vm.traveler = result;
            });
        };
        var unsubscribe = $rootScope.$on('backendApp:travelerUpdate', function(event, result) {
            vm.traveler = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

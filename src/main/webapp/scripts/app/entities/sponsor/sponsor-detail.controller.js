(function() {
    'use strict';

    angular
        .module('backendApp')
        .controller('SponsorDetailController', SponsorDetailController);

    SponsorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Sponsor'];

    function SponsorDetailController($scope, $rootScope, $stateParams, entity, Sponsor) {
        var vm = this;
        vm.sponsor = entity;
        vm.load = function (id) {
            Sponsor.get({id: id}, function(result) {
                vm.sponsor = result;
            });
        };
        var unsubscribe = $rootScope.$on('backendApp:sponsorUpdate', function(event, result) {
            vm.sponsor = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

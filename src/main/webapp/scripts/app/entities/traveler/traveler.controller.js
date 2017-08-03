(function() {
    'use strict';

    angular
        .module('backendApp')
        .controller('TravelerController', TravelerController);

    TravelerController.$inject = ['$scope', '$state', 'Traveler', 'ParseLinks', 'AlertService'];

    function TravelerController ($scope, $state, Traveler, ParseLinks, AlertService) {
        var vm = this;
        vm.loadAll = loadAll;
        vm.loadPage = loadPage;
        vm.predicate = 'id';
        vm.reverse = true;
        vm.page = 1;

        vm.loadAll();

        function loadAll () {
            Traveler.query({
                page: vm.page - 1,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.travelers = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.loadAll();
        }
    }
})();

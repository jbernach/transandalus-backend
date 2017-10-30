(function() {
    'use strict';

    angular
        .module('backendApp')
        .controller('TravelerDeleteController',TravelerDeleteController);

    TravelerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Traveler'];

    function TravelerDeleteController($uibModalInstance, entity, Traveler) {
        var vm = this;
        vm.traveler = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Traveler.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

(function() {
    'use strict';

    angular
        .module('backendApp')
        .controller('SponsorDeleteController',SponsorDeleteController);

    SponsorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sponsor'];

    function SponsorDeleteController($uibModalInstance, entity, Sponsor) {
        var vm = this;
        vm.sponsor = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Sponsor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

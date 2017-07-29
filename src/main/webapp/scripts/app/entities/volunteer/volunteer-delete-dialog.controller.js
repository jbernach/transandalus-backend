(function() {
    'use strict';

    angular
        .module('backendApp')
        .controller('VolunteerDeleteController',VolunteerDeleteController);

    VolunteerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Volunteer'];

    function VolunteerDeleteController($uibModalInstance, entity, Volunteer) {
        var vm = this;
        vm.volunteer = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Volunteer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

(function() {
    'use strict';

    angular
        .module('backendApp')
        .controller('VolunteerDialogController', VolunteerDialogController);

    VolunteerDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Volunteer', '$translate'];

    function VolunteerDialogController ($scope, $stateParams, $uibModalInstance, entity, Volunteer, $translate) {
        var vm = this;

        vm.language = $translate.use();

        vm.volunteer = entity;
        vm.load = function(id) {
            Volunteer.get({id : id}, function(result) {
                vm.volunteer = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('backendApp:volunteerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.volunteer.id !== null) {
                Volunteer.update(vm.volunteer, onSaveSuccess, onSaveError);
            } else {
                Volunteer.save(vm.volunteer, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

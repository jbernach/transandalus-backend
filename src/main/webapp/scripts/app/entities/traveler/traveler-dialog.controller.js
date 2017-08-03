(function() {
    'use strict';

    angular
        .module('backendApp')
        .controller('TravelerDialogController', TravelerDialogController);

    TravelerDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Traveler', '$translate'];

    function TravelerDialogController ($scope, $stateParams, $uibModalInstance, entity, Traveler, $translate) {
        var vm = this;

        vm.language = $translate.use();

        vm.traveler = entity;
        vm.load = function(id) {
            Traveler.get({id : id}, function(result) {
                vm.traveler = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('backendApp:travelerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.traveler.id !== null) {
                Traveler.update(vm.traveler, onSaveSuccess, onSaveError);
            } else {
                Traveler.save(vm.traveler, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

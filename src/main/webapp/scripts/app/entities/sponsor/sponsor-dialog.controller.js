(function() {
    'use strict';

    angular
        .module('backendApp')
        .controller('SponsorDialogController', SponsorDialogController);

    SponsorDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sponsor', '$translate'];

    function SponsorDialogController ($scope, $stateParams, $uibModalInstance, entity, Sponsor, $translate) {
        var vm = this;

        vm.language = $translate.use();

        vm.sponsor = entity;
        vm.load = function(id) {
            Sponsor.get({id : id}, function(result) {
                vm.sponsor = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('backendApp:sponsorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.sponsor.id !== null) {
                Sponsor.update(vm.sponsor, onSaveSuccess, onSaveError);
            } else {
                Sponsor.save(vm.sponsor, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

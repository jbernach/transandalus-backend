(function() {
    'use strict';

    angular
        .module('backendApp')
        .controller('VolunteerDetailController', VolunteerDetailController);

    VolunteerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Volunteer'];

    function VolunteerDetailController($scope, $rootScope, $stateParams, entity, Volunteer) {
        var vm = this;
        vm.volunteer = entity;
        vm.load = function (id) {
            Volunteer.get({id: id}, function(result) {
                vm.volunteer = result;
            });
        };
        var unsubscribe = $rootScope.$on('backendApp:volunteerUpdate', function(event, result) {
            vm.volunteer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

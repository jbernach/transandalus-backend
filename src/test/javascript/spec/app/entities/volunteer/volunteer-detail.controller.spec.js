'use strict';

describe('Controller Tests', function() {

    describe('Volunteer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockVolunteer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockVolunteer = jasmine.createSpy('MockVolunteer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Volunteer': MockVolunteer
            };
            createController = function() {
                $injector.get('$controller')("VolunteerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'backendApp:volunteerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

'use strict';

describe('Controller Tests', function() {

    describe('MenuItem Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMenuItem, MockMenu;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMenuItem = jasmine.createSpy('MockMenuItem');
            MockMenu = jasmine.createSpy('MockMenu');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MenuItem': MockMenuItem,
                'Menu': MockMenu
            };
            createController = function() {
                $injector.get('$controller')("MenuItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'backendApp:menuItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

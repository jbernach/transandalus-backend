'use strict';

describe('Controller Tests', function() {

    describe('Stage Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStage, MockProvince;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStage = jasmine.createSpy('MockStage');
            MockProvince = jasmine.createSpy('MockProvince');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Stage': MockStage,
                'Province': MockProvince
            };
            createController = function() {
                $injector.get('$controller')("StageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'backendApp:stageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

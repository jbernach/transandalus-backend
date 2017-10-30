(function() {
    'use strict';

    angular
        .module('backendApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('traveler', {
            parent: 'entity',
            url: '/traveler',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'backendApp.traveler.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/entities/traveler/travelers.html',
                    controller: 'TravelerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {

                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('traveler');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('traveler-detail', {
            parent: 'entity',
            url: '/traveler/{id:int}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'backendApp.traveler.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/entities/traveler/traveler-detail.html',
                    controller: 'TravelerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('traveler');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Traveler', function($stateParams, Traveler) {
                    return Traveler.get({id : $stateParams.id});
                }]
            }
        })
        .state('traveler.new', {
            parent: 'traveler',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/entities/traveler/traveler-dialog.html',
                    controller: 'TravelerDialogController',
                    controllerAs: 'vm',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                from: null,
                                text: null,
                                image: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('traveler', null, { reload: true });
                }, function() {
                    $state.go('traveler');
                });
            }]
        })
        .state('traveler.edit', {
            parent: 'traveler',
            url: '/{id:int}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/entities/traveler/traveler-dialog.html',
                    controller: 'TravelerDialogController',
                    controllerAs: 'vm',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        entity: ['Traveler', function(Traveler) {
                            return Traveler.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('traveler', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('traveler.delete', {
            parent: 'traveler',
            url: '/{id:int}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/entities/traveler/traveler-delete-dialog.html',
                    controller: 'TravelerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    backdrop: 'static',
                    resolve: {
                        entity: ['Traveler', function(Traveler) {
                            return Traveler.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('traveler', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

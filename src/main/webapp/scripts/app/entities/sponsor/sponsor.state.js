(function() {
    'use strict';

    angular
        .module('backendApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sponsor', {
            parent: 'entity',
            url: '/sponsor',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'backendApp.sponsor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/entities/sponsor/sponsors.html',
                    controller: 'SponsorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {

                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sponsor');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sponsor-detail', {
            parent: 'entity',
            url: '/sponsor/{id:int}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'backendApp.sponsor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/entities/sponsor/sponsor-detail.html',
                    controller: 'SponsorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sponsor');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Sponsor', function($stateParams, Sponsor) {
                    return Sponsor.get({id : $stateParams.id});
                }]
            }
        })
        .state('sponsor.new', {
            parent: 'sponsor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/entities/sponsor/sponsor-dialog.html',
                    controller: 'SponsorDialogController',
                    controllerAs: 'vm',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                link: null,
                                text: null,
                                image: null,
                                from_date: null,
                                to_date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sponsor', null, { reload: true });
                }, function() {
                    $state.go('sponsor');
                });
            }]
        })
        .state('sponsor.edit', {
            parent: 'sponsor',
            url: '/{id:int}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/entities/sponsor/sponsor-dialog.html',
                    controller: 'SponsorDialogController',
                    controllerAs: 'vm',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        entity: ['Sponsor', function(Sponsor) {
                            return Sponsor.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('sponsor', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sponsor.delete', {
            parent: 'sponsor',
            url: '/{id:int}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/entities/sponsor/sponsor-delete-dialog.html',
                    controller: 'SponsorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    backdrop: 'static',
                    resolve: {
                        entity: ['Sponsor', function(Sponsor) {
                            return Sponsor.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('sponsor', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('backendApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('volunteer', {
            parent: 'entity',
            url: '/volunteer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'backendApp.volunteer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/entities/volunteer/volunteers.html',
                    controller: 'VolunteerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {

                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('volunteer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('volunteer-detail', {
            parent: 'entity',
            url: '/volunteer/{id:int}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'backendApp.volunteer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/entities/volunteer/volunteer-detail.html',
                    controller: 'VolunteerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('volunteer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Volunteer', function($stateParams, Volunteer) {
                    return Volunteer.get({id : $stateParams.id});
                }]
            }
        })
        .state('volunteer.new', {
            parent: 'volunteer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/entities/volunteer/volunteer-dialog.html',
                    controller: 'VolunteerDialogController',
                    controllerAs: 'vm',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                text: null,
                                image: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('volunteer', null, { reload: true });
                }, function() {
                    $state.go('volunteer');
                });
            }]
        })
        .state('volunteer.edit', {
            parent: 'volunteer',
            url: '/{id:int}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/entities/volunteer/volunteer-dialog.html',
                    controller: 'VolunteerDialogController',
                    controllerAs: 'vm',
                    size: 'lg',
                    backdrop: 'static',
                    resolve: {
                        entity: ['Volunteer', function(Volunteer) {
                            return Volunteer.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('volunteer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('volunteer.delete', {
            parent: 'volunteer',
            url: '/{id:int}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/entities/volunteer/volunteer-delete-dialog.html',
                    controller: 'VolunteerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    backdrop: 'static',
                    resolve: {
                        entity: ['Volunteer', function(Volunteer) {
                            return Volunteer.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('volunteer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

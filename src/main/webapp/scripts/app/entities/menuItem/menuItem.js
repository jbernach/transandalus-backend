'use strict';

angular.module('backendApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('menuItem', {
                parent: 'entity',
                url: '/menuItems',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'backendApp.menuItem.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/menuItem/menuItems.html',
                        controller: 'MenuItemController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('menuItem');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('menuItem.detail', {
                parent: 'entity',
                url: '/menuItem/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'backendApp.menuItem.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/menuItem/menuItem-detail.html',
                        controller: 'MenuItemDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('menuItem');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'MenuItem', function($stateParams, MenuItem) {
                        return MenuItem.get({id : $stateParams.id});
                    }]
                }
            })
            .state('menuItem.new', {
                parent: 'menuItem',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/menuItem/menuItem-dialog.html',
                        controller: 'MenuItemDialogController',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                return {
                                    text: null,
                                    url: null,
                                    id: null,
                                    order: 1
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('menuItem', null, { reload: true });
                    }, function() {
                        $state.go('menuItem');
                    })
                }]
            })
            .state('menuItem.edit', {
                parent: 'menuItem',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/menuItem/menuItem-dialog.html',
                        controller: 'MenuItemDialogController',
                        size: 'md',
                        resolve: {
                            entity: ['MenuItem', function(MenuItem) {
                                return MenuItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('menuItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('menuItem.delete', {
                parent: 'menuItem',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/menuItem/menuItem-delete-dialog.html',
                        controller: 'MenuItemDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MenuItem', function(MenuItem) {
                                return MenuItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('menuItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

'use strict';

angular.module('backendApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('menu', {
                parent: 'entity',
                url: '/menus',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'backendApp.menu.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/menu/menus.html',
                        controller: 'MenuController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('menu');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('menu.detail', {
                parent: 'entity',
                url: '/menu/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'backendApp.menu.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/menu/menu-detail.html',
                        controller: 'MenuDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('menu');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Menu', function($stateParams, Menu) {
                        return Menu.get({id : $stateParams.id});
                    }]
                }
            })
            .state('menu.new', {
                parent: 'menu',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/menu/menu-dialog.html',
                        controller: 'MenuDialogController',
                        size: 'md',
                        backdrop: 'static',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('menu', null, { reload: true });
                    }, function() {
                        $state.go('menu');
                    })
                }]
            })
            .state('menu.edit', {
                parent: 'menu',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/menu/menu-dialog.html',
                        controller: 'MenuDialogController',
                        size: 'md',
                        backdrop: 'static',
                        resolve: {
                            entity: ['Menu', function(Menu) {
                                return Menu.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('menu', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('menu.delete', {
                parent: 'menu',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/menu/menu-delete-dialog.html',
                        controller: 'MenuDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Menu', function(Menu) {
                                return Menu.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('menu', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

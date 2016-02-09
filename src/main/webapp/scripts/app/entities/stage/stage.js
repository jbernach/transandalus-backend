'use strict';

angular.module('backendApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stage', {
                parent: 'entity',
                url: '/stages',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'backendApp.stage.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stage/stages.html',
                        controller: 'StageController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stage');
                        $translatePartialLoader.addPart('difficulty');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stage.detail', {
                parent: 'entity',
                url: '/stage/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'backendApp.stage.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stage/stage-detail.html',
                        controller: 'StageDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stage');
                        $translatePartialLoader.addPart('difficulty');
                        $translatePartialLoader.addPart('difficulty');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Stage', function($stateParams, Stage) {
                        return Stage.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stage.new', {
                parent: 'stage',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stage/stage-dialog.html',
                        controller: 'StageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    distanceTotal: null,
                                    distanceRoad: null,
                                    estimatedTime: null,
                                    elevation: null,
                                    difficultyPhys: null,
                                    difficultyTech: null,
                                    galleryURL: null,
                                    imageUrl: null,
                                    track: {id:null, name: null, content: null, contentType: null},
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stage', null, { reload: true });
                    }, function() {
                        $state.go('stage');
                    })
                }]
            })
            .state('stage.edit', {
                parent: 'stage',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stage/stage-dialog.html',
                        controller: 'StageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Stage', function(Stage) {
                                return Stage.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stage', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('stage.delete', {
                parent: 'stage',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stage/stage-delete-dialog.html',
                        controller: 'StageDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Stage', function(Stage) {
                                return Stage.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stage', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });

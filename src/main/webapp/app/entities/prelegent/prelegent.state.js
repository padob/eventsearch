(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prelegent', {
            parent: 'entity',
            url: '/prelegent?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'eventsearchApp.prelegent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prelegent/prelegents.html',
                    controller: 'PrelegentController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prelegent');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('prelegent-detail', {
            parent: 'prelegent',
            url: '/prelegent/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'eventsearchApp.prelegent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prelegent/prelegent-detail.html',
                    controller: 'PrelegentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prelegent');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Prelegent', function($stateParams, Prelegent) {
                    return Prelegent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'prelegent',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('prelegent-detail.edit', {
            parent: 'prelegent-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prelegent/prelegent-dialog.html',
                    controller: 'PrelegentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prelegent', function(Prelegent) {
                            return Prelegent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prelegent.new', {
            parent: 'prelegent',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prelegent/prelegent-dialog.html',
                    controller: 'PrelegentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                bio: null,
                                websiteUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prelegent', null, { reload: 'prelegent' });
                }, function() {
                    $state.go('prelegent');
                });
            }]
        })
        .state('prelegent.edit', {
            parent: 'prelegent',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prelegent/prelegent-dialog.html',
                    controller: 'PrelegentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prelegent', function(Prelegent) {
                            return Prelegent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prelegent', null, { reload: 'prelegent' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prelegent.delete', {
            parent: 'prelegent',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prelegent/prelegent-delete-dialog.html',
                    controller: 'PrelegentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Prelegent', function(Prelegent) {
                            return Prelegent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prelegent', null, { reload: 'prelegent' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

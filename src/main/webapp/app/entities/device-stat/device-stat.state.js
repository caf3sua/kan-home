(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('device-stat', {
            parent: 'entity',
            url: '/device-stat?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kanHomeApp.deviceStat.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/device-stat/device-stats.html',
                    controller: 'DeviceStatController',
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
                    $translatePartialLoader.addPart('deviceStat');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('device-stat-detail', {
            parent: 'device-stat',
            url: '/device-stat/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kanHomeApp.deviceStat.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/device-stat/device-stat-detail.html',
                    controller: 'DeviceStatDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('deviceStat');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DeviceStat', function($stateParams, DeviceStat) {
                    return DeviceStat.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'device-stat',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('device-stat-detail.edit', {
            parent: 'device-stat-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/device-stat/device-stat-dialog.html',
                    controller: 'DeviceStatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DeviceStat', function(DeviceStat) {
                            return DeviceStat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('device-stat.new', {
            parent: 'device-stat',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/device-stat/device-stat-dialog.html',
                    controller: 'DeviceStatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                clientid: null,
                                time: null,
                                u: null,
                                wQ: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('device-stat', null, { reload: 'device-stat' });
                }, function() {
                    $state.go('device-stat');
                });
            }]
        })
        .state('device-stat.edit', {
            parent: 'device-stat',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/device-stat/device-stat-dialog.html',
                    controller: 'DeviceStatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DeviceStat', function(DeviceStat) {
                            return DeviceStat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('device-stat', null, { reload: 'device-stat' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('device-stat.delete', {
            parent: 'device-stat',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/device-stat/device-stat-delete-dialog.html',
                    controller: 'DeviceStatDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DeviceStat', function(DeviceStat) {
                            return DeviceStat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('device-stat', null, { reload: 'device-stat' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

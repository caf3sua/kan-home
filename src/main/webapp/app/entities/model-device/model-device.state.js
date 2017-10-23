(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('model-device', {
            parent: 'entity',
            url: '/model-device?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kanHomeApp.modelDevice.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-device/model-devices.html',
                    controller: 'ModelDeviceController',
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
                    $translatePartialLoader.addPart('modelDevice');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('model-device-detail', {
            parent: 'model-device',
            url: '/model-device/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kanHomeApp.modelDevice.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/model-device/model-device-detail.html',
                    controller: 'ModelDeviceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('modelDevice');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ModelDevice', function($stateParams, ModelDevice) {
                    return ModelDevice.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'model-device',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('model-device-detail.edit', {
            parent: 'model-device-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-device/model-device-dialog.html',
                    controller: 'ModelDeviceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelDevice', function(ModelDevice) {
                            return ModelDevice.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-device.new', {
            parent: 'model-device',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-device/model-device-dialog.html',
                    controller: 'ModelDeviceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                model: null,
                                waranty: null,
                                imageUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('model-device', null, { reload: 'model-device' });
                }, function() {
                    $state.go('model-device');
                });
            }]
        })
        .state('model-device.edit', {
            parent: 'model-device',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-device/model-device-dialog.html',
                    controller: 'ModelDeviceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ModelDevice', function(ModelDevice) {
                            return ModelDevice.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-device', null, { reload: 'model-device' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('model-device.delete', {
            parent: 'model-device',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/model-device/model-device-delete-dialog.html',
                    controller: 'ModelDeviceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ModelDevice', function(ModelDevice) {
                            return ModelDevice.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('model-device', null, { reload: 'model-device' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

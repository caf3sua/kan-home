(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('device', {
            parent: 'management',
            url: '/device?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'kanHomeApp.device.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/device/devices.html',
                    controller: 'DeviceController',
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
                    $translatePartialLoader.addPart('device');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('device-detail', {
            parent: 'device',
            url: '/device/{abbrName}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'kanHomeApp.device.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/device/device-detail.html',
                    controller: 'DeviceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('device');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Device', function($stateParams, Device) {
                    return Device.getByAbbrname({abbrName : $stateParams.abbrName}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'device',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('device-detail.edit', {
            parent: 'device-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/device/device-dialog.html',
                    controller: 'DeviceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Device', function(Device) {
                            return Device.getByAbbrname({abbrName : $stateParams.abbrName}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('device.new', {
            parent: 'device',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/device/device-dialog.html',
                    controller: 'DeviceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                abbrName: null,
                                model: null,
                                firmware: null,
                                passcode: null,
                                countryCode: null,
                                cityCode: null,
                                wardCode: null,
                                zipCode: null,
                                gpsX: null,
                                gpsY: null,
                                srvName: null,
                                srvPort: null,
                                repotIntv: null,
                                saveIntv: null,
                                keepalive: null,
                                distCode: null,
                                cfgNum: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('device', null, { reload: 'device' });
                }, function() {
                    $state.go('device');
                });
            }]
        })
        .state('device.edit', {
            parent: 'device',
            url: '/{abbrName}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/device/device-dialog.html',
                    controller: 'DeviceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Device', function(Device) {
                            return Device.getByAbbrname({abbrName : $stateParams.abbrName}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('device', null, { reload: 'device' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('device.delete', {
            parent: 'device',
            url: '/delete/{abbrNames}',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/device/device-delete-dialog.html',
                    controller: 'DeviceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
//                        entity: ['Device', function(Device) {
//                            return Device.get({id : $stateParams.id}).$promise;
//                        }]
                    	abbrNames: function () {
                            return $stateParams.abbrNames;
                        }
                    }
                }).result.then(function() {
                    $state.go('device', null, { reload: 'device' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

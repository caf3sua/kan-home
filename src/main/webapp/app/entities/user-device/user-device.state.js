(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .config(stateConfig);
    
    stateConfig.$inject = ['$stateProvider'];
    
    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-device', {
            parent: 'management',
            url: '/user-device',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'kanHomeApp.userDevice.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-device/user-device.html',
                    controller: 'UserDeviceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userDevice');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }

})();

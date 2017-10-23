(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .config(stateConfig);
    
    stateConfig.$inject = ['$stateProvider'];
    
    function stateConfig($stateProvider) {
        $stateProvider
        .state('map', {
            parent: 'management',
            url: '/map',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'kanHomeApp.map.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/map/map.html',
                    controller: 'MapController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('map');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('signin', {
            parent: 'account',
            url: '/signin',
            data: {
                authorities: [],
                pageTitle: 'login.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/signin/signin.html',
                    controller: 'SigninController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('signin');
                    return $translate.refresh();
                }]
            }
        });
    }
})();

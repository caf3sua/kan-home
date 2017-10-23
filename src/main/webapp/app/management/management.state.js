(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('management', {
            abstract: true,
            parent: 'app'
        });
    }
})();
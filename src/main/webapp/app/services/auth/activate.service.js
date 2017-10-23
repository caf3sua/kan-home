(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .factory('Activate', Activate);

    Activate.$inject = ['$resource'];

    function Activate ($resource) {
        var service = $resource('api/activate', {}, {
            'get': { method: 'GET', params: {}, isArray: false},
            'getSMS': { url: 'api/account/activate', method: 'GET', params: {}, isArray: false},
            'resendSMS': { url: 'api/account/resend_sms', method: 'GET', params: {}, isArray: false},
        });

        return service;
    }
})();

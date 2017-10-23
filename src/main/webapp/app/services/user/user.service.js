(function () {
    'use strict';

    angular
        .module('kanHomeApp')
        .factory('User', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        var service = $resource('api/users/:login', {}, {
        	'search': {url : 'api/users-search', method: 'POST', isArray: true},
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'},
            'updateDevice': { url: 'api/users_update_devices', method:'POST' },
            'deleteUsers': { url: 'api/delete_users', method:'POST' },
            'resetPass': { url: 'api/reset_pass', method:'POST' },
        });

        return service;
    }
})();

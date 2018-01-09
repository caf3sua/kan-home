(function() {
    'use strict';
    angular
        .module('kanHomeApp')
        .factory('Device', Device);

    Device.$inject = ['$resource'];

    function Device ($resource) {
        var resourceUrl =  'api/devices/:id';

        return $resource(resourceUrl, {}, {
        	'searchWithGridView': {url : 'api/devices-search-on-grid', method: 'POST', isArray: true},
        	'searchWithMapData': {url : 'api/devices-search-on-map', method: 'POST', isArray: true},
        	'queryWithMapData': {url : 'api/devices-on-map', method: 'GET', isArray: true},
        	'queryWithUserData': {url : 'api/device-users', method: 'POST', isArray: true},
        	'queryWithSimpleData': {url : 'api/devices-simple-data', method: 'GET', isArray: true},
        	'queryWithSimpleDataAsMap': {url : 'api/devices-simple-data-as-map', method: 'GET', isArray: false},
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'getByAbbrname': {
            	url : 'api/get-by-abbrName/:abbrName',
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'deleteDevices': {url : 'api/delete-devices', method: 'POST'},
        });
    }
})();

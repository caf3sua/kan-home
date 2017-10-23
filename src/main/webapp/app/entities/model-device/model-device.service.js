(function() {
    'use strict';
    angular
        .module('kanHomeApp')
        .factory('ModelDevice', ModelDevice);

    ModelDevice.$inject = ['$resource'];

    function ModelDevice ($resource) {
        var resourceUrl =  'api/model-devices/:id';

        return $resource(resourceUrl, {}, {
        	'queryAll': { url: 'api/model-devices-all', method: 'GET', isArray: true},
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
            'getByModel': {
            	url: 'api/model-devices-by-model/:model',
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

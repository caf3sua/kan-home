(function() {
    'use strict';
    angular
        .module('kanHomeApp')
        .factory('DeviceStat', DeviceStat);

    DeviceStat.$inject = ['$resource'];

    function DeviceStat ($resource) {
        var resourceUrl =  'api/device-stats/:id';

        return $resource(resourceUrl, {}, {
        	'queryByIds': { url: 'api/device-stats-by-ids', method: 'POST', isArray: true},
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
            'update': { method:'PUT' }
        });
    }
})();

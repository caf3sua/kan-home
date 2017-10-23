(function() {
    'use strict';
    angular
        .module('kanHomeApp')
        .factory('City', City);

    City.$inject = ['$resource'];

    function City ($resource) {
        var resourceUrl =  'api/cities/:id';

        return $resource(resourceUrl, {}, {
        	'queryAll': { url: 'api/cities-all', method: 'GET', isArray: true},
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

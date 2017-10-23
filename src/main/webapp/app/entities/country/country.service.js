(function() {
    'use strict';
    angular
        .module('kanHomeApp')
        .factory('Country', Country);

    Country.$inject = ['$resource'];

    function Country ($resource) {
        var resourceUrl =  'api/countries/:id';

        return $resource(resourceUrl, {}, {
        	'queryAll': { url: 'api/countries-all', method: 'GET', isArray: true},
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
            'getbyCountryCode': {
            	url : 'api/countries-by-code/:countryCode',
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

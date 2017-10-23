(function() {
    'use strict';
    angular
        .module('kanHomeApp')
        .factory('Summary', Summary);

    Summary.$inject = ['$resource'];

    function Summary ($resource) {
        var resourceUrl =  'api/summary-data';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: false}
        });
    }
})();

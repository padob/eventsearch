(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .factory('SeriesSearch', SeriesSearch);

    SeriesSearch.$inject = ['$resource'];

    function SeriesSearch($resource) {
        var resourceUrl =  'api/_search/series/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .factory('PrelegentSearch', PrelegentSearch);

    PrelegentSearch.$inject = ['$resource'];

    function PrelegentSearch($resource) {
        var resourceUrl =  'api/_search/prelegents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

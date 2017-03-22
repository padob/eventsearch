(function() {
    'use strict';
    angular
        .module('eventsearchApp')
        .factory('Prelegent', Prelegent);

    Prelegent.$inject = ['$resource'];

    function Prelegent ($resource) {
        var resourceUrl =  'api/prelegents/:id';

        return $resource(resourceUrl, {}, {
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

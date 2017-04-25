(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .controller('SeriesDetailController', SeriesDetailController);

    SeriesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Series', 'Event'];

    function SeriesDetailController($scope, $rootScope, $stateParams, previousState, entity, Series, Event) {
        var vm = this;

        vm.series = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('eventsearchApp:seriesUpdate', function(event, result) {
            vm.series = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

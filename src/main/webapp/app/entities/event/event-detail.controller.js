(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .controller('EventDetailController', EventDetailController);

    EventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Event', 'Topic', 'City', 'Series'];

    function EventDetailController($scope, $rootScope, $stateParams, previousState, entity, Event, Topic, City, Series) {
        var vm = this;

        vm.event = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('eventsearchApp:eventUpdate', function(event, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

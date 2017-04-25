(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .controller('TopicDetailController', TopicDetailController);

    TopicDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Topic', 'Event'];

    function TopicDetailController($scope, $rootScope, $stateParams, previousState, entity, Topic, Event) {
        var vm = this;

        vm.topic = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('eventsearchApp:topicUpdate', function(event, result) {
            vm.topic = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

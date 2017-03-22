(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .controller('PrelegentDetailController', PrelegentDetailController);

    PrelegentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Prelegent'];

    function PrelegentDetailController($scope, $rootScope, $stateParams, previousState, entity, Prelegent) {
        var vm = this;

        vm.prelegent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('eventsearchApp:prelegentUpdate', function(event, result) {
            vm.prelegent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

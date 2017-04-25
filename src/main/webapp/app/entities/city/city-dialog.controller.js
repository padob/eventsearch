(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .controller('CityDialogController', CityDialogController);

    CityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'City', 'Event'];

    function CityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, City, Event) {
        var vm = this;

        vm.city = entity;
        vm.clear = clear;
        vm.save = save;
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.city.id !== null) {
                City.update(vm.city, onSaveSuccess, onSaveError);
            } else {
                City.save(vm.city, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('eventsearchApp:cityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

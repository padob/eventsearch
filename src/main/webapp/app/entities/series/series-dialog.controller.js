(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .controller('SeriesDialogController', SeriesDialogController);

    SeriesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Series', 'Event'];

    function SeriesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Series, Event) {
        var vm = this;

        vm.series = entity;
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
            if (vm.series.id !== null) {
                Series.update(vm.series, onSaveSuccess, onSaveError);
            } else {
                Series.save(vm.series, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('eventsearchApp:seriesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

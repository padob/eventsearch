(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .controller('PrelegentDialogController', PrelegentDialogController);

    PrelegentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Prelegent'];

    function PrelegentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Prelegent) {
        var vm = this;

        vm.prelegent = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.prelegent.id !== null) {
                Prelegent.update(vm.prelegent, onSaveSuccess, onSaveError);
            } else {
                Prelegent.save(vm.prelegent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('eventsearchApp:prelegentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .controller('PrelegentDeleteController',PrelegentDeleteController);

    PrelegentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Prelegent'];

    function PrelegentDeleteController($uibModalInstance, entity, Prelegent) {
        var vm = this;

        vm.prelegent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Prelegent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

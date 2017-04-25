(function() {
    'use strict';

    angular
        .module('eventsearchApp')
        .controller('TopicDialogController', TopicDialogController);

    TopicDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Topic', 'Event'];

    function TopicDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Topic, Event) {
        var vm = this;

        vm.topic = entity;
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
            if (vm.topic.id !== null) {
                Topic.update(vm.topic, onSaveSuccess, onSaveError);
            } else {
                Topic.save(vm.topic, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('eventsearchApp:topicUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

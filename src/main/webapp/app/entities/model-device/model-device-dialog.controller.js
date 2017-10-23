(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('ModelDeviceDialogController', ModelDeviceDialogController);

    ModelDeviceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ModelDevice'];

    function ModelDeviceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ModelDevice) {
        var vm = this;

        vm.modelDevice = entity;
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
            if (vm.modelDevice.id !== null) {
                ModelDevice.update(vm.modelDevice, onSaveSuccess, onSaveError);
            } else {
                ModelDevice.save(vm.modelDevice, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kanHomeApp:modelDeviceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('ModelDeviceDeleteController',ModelDeviceDeleteController);

    ModelDeviceDeleteController.$inject = ['$uibModalInstance', 'entity', 'ModelDevice'];

    function ModelDeviceDeleteController($uibModalInstance, entity, ModelDevice) {
        var vm = this;

        vm.modelDevice = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ModelDevice.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

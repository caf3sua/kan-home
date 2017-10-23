(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('DeviceStatDeleteController',DeviceStatDeleteController);

    DeviceStatDeleteController.$inject = ['$uibModalInstance', 'entity', 'DeviceStat'];

    function DeviceStatDeleteController($uibModalInstance, entity, DeviceStat) {
        var vm = this;

        vm.deviceStat = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DeviceStat.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

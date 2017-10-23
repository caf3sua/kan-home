(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('DeviceDeleteController',DeviceDeleteController);

    DeviceDeleteController.$inject = ['$uibModalInstance', 'abbrNames', 'Device'];

    function DeviceDeleteController($uibModalInstance, abbrNames, Device) {
        var vm = this;

        vm.abbrNames = abbrNames;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete () {
        	Device.deleteDevices(abbrNames,
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

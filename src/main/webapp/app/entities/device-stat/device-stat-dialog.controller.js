(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('DeviceStatDialogController', DeviceStatDialogController);

    DeviceStatDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DeviceStat'];

    function DeviceStatDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DeviceStat) {
        var vm = this;

        vm.deviceStat = entity;
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
            if (vm.deviceStat.id !== null) {
                DeviceStat.update(vm.deviceStat, onSaveSuccess, onSaveError);
            } else {
                DeviceStat.save(vm.deviceStat, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kanHomeApp:deviceStatUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

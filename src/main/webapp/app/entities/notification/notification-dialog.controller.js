(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('NotificationDialogController', NotificationDialogController);

    NotificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Notification'];

    function NotificationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Notification) {
        var vm = this;

        vm.notification = entity;
        vm.clear = clear;
        vm.save = save;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        vm.datePickerOpenStatus.schedule = false;
        
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
        	// Convert date -> time
        	if (vm.notification.time != null && vm.notification.time != undefined) {
        		vm.notification.epoch = vm.notification.time.getTime();
        	}
        	console.log('epoch:' + vm.notification.epoch);
        	
            vm.isSaving = true;
            if (vm.notification.id !== null) {
                Notification.update(vm.notification, onSaveSuccess, onSaveError);
            } else {
                Notification.save(vm.notification, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kanHomeApp:notificationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
            publish();
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

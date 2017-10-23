(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('DeviceDialogController', DeviceDialogController);

    DeviceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Device', 'Country'];

    function DeviceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Device, Country) {
        var vm = this;

        vm.device = entity;
        vm.clear = clear;
        vm.save = save;

        vm.countries = [];
        
        // Init controller
		(function initController() {
			loadAllCountry();
			
			vm.device.countryCode = vm.device.countryCode.toString();
	    })();
		
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function loadAllCountry() {
        	Country.queryAll({}, onSaveSuccess, onSaveError);
            
    		function onSaveSuccess (result) {
    			vm.countries = result;
            };

            function onSaveError () {
            }
        }
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.device.id !== null) {
                Device.update(vm.device, onSaveSuccess, onSaveError);
            } else {
                Device.save(vm.device, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('kanHomeApp:deviceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

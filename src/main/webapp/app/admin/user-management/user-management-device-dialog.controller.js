(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('UserManagementDeviceController', UserManagementDeviceController)
        .filter('propsFilter', function() {
		  return function(items, props) {
		    var out = [];
		
		    if (angular.isArray(items)) {
		      var keys = Object.keys(props);
		
		      items.forEach(function(item) {
		        var itemMatches = false;
		
		        for (var i = 0; i < keys.length; i++) {
		          var prop = keys[i];
		          var text = props[prop].toLowerCase();
		          if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
		            itemMatches = true;
		            break;
		          }
		        }
		
		        if (itemMatches) {
		          out.push(item);
		        }
		      });
		    } else {
		      // Let the output be the input untouched
		      out = items;
		    }
		
		    return out;
		  };
		});

    UserManagementDeviceController.$inject = ['$scope', '$http', '$timeout', '$interval', '$uibModalInstance', 'user', 'User', 'Device', '$localStorage'];

    function UserManagementDeviceController ($scope, $http, $timeout, $interval, $uibModalInstance, user, User, Device, $localStorage) {
        var vm = this;

        vm.user = user;
        vm.clear = clear;
        vm.confirmUpdateDevice = confirmUpdateDevice;
        // Select
        vm.devices = [];
        vm.selectedDevices = [];
        vm.disableBtn = true;
        vm.addDevice = addDevice;
        vm.removeDevice = removeDevice;
               
        // Init controller
		(function initController() {
	        // Init
	    })();
		
		angular.element(document).ready(function () {
			loadAddSimpleDevice();
		});
		
		function removeDevice(device) {
			// Remove from selected and add to list parent again
			var itemDel = [];
    			itemDel.push(device);
			removeEleFromArray(vm.selectedDevices, itemDel);
			
			vm.devices.push(device);
		}
		
        function loadAddSimpleDevice() {
        		// Load from local
        		var devices = $localStorage.all_simple_devices || [];
        		vm.devices = angular.copy(devices);
        		
    			// publish data for selectedDevices
        		angular.forEach(vm.user.userDevices, function (ud, key) {
        			angular.forEach(vm.devices, function (device, key) {
	        			if (device.id === ud.id) {
	        				console.log(device);
	        				vm.selectedDevices.push(device);
	        			}
	        		});
	        });
	        	vm.disableBtn = false;
        		console.log('loadAddSimpleDevice');
	        	removeEleFromArray(vm.devices, vm.selectedDevices);
        }
        
        function addDevice(device) {
        		vm.selectedDevices.push(device);
        		var itemDel = [];
        		itemDel.push(device);
        		removeEleFromArray(vm.devices, itemDel);
        }

        function removeEleFromArray(parentItems, delItems) {
	        	angular.forEach(delItems, function (item, key) {
	        		var index = parentItems.indexOf(item);
	        		parentItems.splice(index, 1);
	    		});
        		
        }
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmUpdateDevice () {
        	angular.forEach(vm.selectedDevices, function (device, key) {
        		device.name = device.abbrName;
            });
        	vm.user.userDevices = vm.selectedDevices;
        	
        	//User.save(vm.user, onSaveSuccess, onSaveError);
            User.updateDevice(vm.user,
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

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

    UserManagementDeviceController.$inject = ['$scope', '$http', '$timeout', '$interval', '$uibModalInstance', 'user', 'User', 'Device'];

    function UserManagementDeviceController ($scope, $http, $timeout, $interval, $uibModalInstance, user, User, Device) {
        var vm = this;

        vm.user = user;
        vm.clear = clear;
        vm.confirmUpdateDevice = confirmUpdateDevice;
        // Select
        vm.devices = [];
        vm.selectedDevices = [];
        vm.disableBtn = true;
               
        // Init controller
		(function initController() {
	        // Init
	        loadAddSimpleDevice();
	    })();

        
        function loadAddSimpleDevice() {
        	Device.queryWithSimpleData({}, onSaveSuccess, onSaveError);
            
            function onSaveSuccess (result) {
            	vm.devices = result;
            	// publish data for selectedDevices
            	angular.forEach(vm.devices, function (device, key) {
            		angular.forEach(vm.user.userDevices, function (ud, key) {
            			if (device.id == ud.id) {
            				vm.selectedDevices.push(device);
            			}
            		});
                });
            	
            	vm.disableBtn = false;
            }

            function onSaveError () {
            }
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

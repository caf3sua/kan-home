(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('DeviceController', DeviceController);

    DeviceController.$inject = ['$rootScope', '$scope', '$state', 'Device', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'DeviceStat', '$mdSidenav', 'Country', 'ModelDevice'];

    function DeviceController($rootScope, $scope, $state, Device, ParseLinks, AlertService, paginationConstants, pagingParams, DeviceStat, $mdSidenav, Country, ModelDevice) {

        var vm = this;

        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        
        vm.isSearchShow = true;
        vm.toogleSearch = toogleSearch;
        vm.searchAll = searchAll;
        vm.cleanSearch = cleanSearch;
        vm.showAllUser = showAllUser;
        vm.searchFilter = searchFilter;
        
        vm.deviceStats = [];
        vm.countries = [];
        vm.models = [];
        
        vm.currentSearch = {};
        
        vm.selectedItem = [];
        vm.checkAll = checkAll;
        vm.checkItem = checkItem;
        
        // Init controller
		(function initController() {
			// INIT
	        searchAll();
	        loadAllCountry();
	        loadAllModelDevice();
	    })();
        
		function resetCheckbox() {
			vm.selectedItem = [];
			$('#chk-all').prop('checked', false);
			$('.table-responsive tr td input[type="checkbox"]').each(function(){
				$(this).prop('checked', false);
			});
		}
		
		function checkAll() {
			if ($('#chk-all').is(':checked')) {
				console.log('checked');
				// Get all id and push into vm.selectedItem
				$('.table-responsive tr td input[type="checkbox"]').each(function(){
					$(this).prop('checked', true);
					vm.selectedItem.push($(this).attr('data'));
				});
			} else {
				console.log('uncheck');
				$('.table-responsive tr td input[type="checkbox"]').each(function(){
					$(this).prop('checked', false);
				});
				
				vm.selectedItem = [];
			}
		}
		
		function checkItem() {
			vm.selectedItem = [];
			var isSelectAll = true;
			$('.table-responsive tr td input[type="checkbox"]').each(function() {
				var isCheck = $(this).prop('checked');
				if (isCheck) {
					vm.selectedItem.push($(this).attr('data'));
				} else {
					isSelectAll = false;
				}
			});
			
			if (isSelectAll) {
				$('#chk-all').prop('checked', true);
			} else {
				$('#chk-all').prop('checked', false);
			}
		}
        
        
        function showAllUser(users) {
        	var result = "";
        	angular.forEach(users, function (user, key) {
        		result = result + user.username + '<br>';
            });
        	
        	return result;
        }
        
        function cleanSearch() {
        	resetCheckbox();
        	storeFilterCondition(null);
        	vm.currentSearch = {};
        	searchAll();
        }
        
        function loadAllModelDevice() {
        	ModelDevice.queryAll({}, onSaveSuccess, onSaveError);
            
    		function onSaveSuccess (result) {
    			vm.models = result;
            };

            function onSaveError () {
            }
        }

        function loadAllCountry() {
        	Country.queryAll({}, onSaveSuccess, onSaveError);
            
    		function onSaveSuccess (result) {
    			vm.countries = result;
            };

            function onSaveError () {
            }
        }
        
        function toogleSearch() {
        	vm.isSearchShow = !vm.isSearchShow;
        }
        
        function loadAllUserInDevice() {
        	// Call API
        	Device.queryWithUserData(vm.devices, onSuccess, onError);
        	function onSuccess(data) {
        		angular.forEach(vm.devices, function (device, key) {
        			angular.forEach(data, function (stat, keyStat) {
        				// wQ
        				if (device.id == stat.id) {
        					device.users = stat.users;
        				}
                    });
                });
        		
        		//$scope.$apply();
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        
//        function loadAllStatus() {
//        	angular.forEach(vm.devices, function (device, key) {
//        		var deviceStat = {};
//        		deviceStat.id = device.id;
//        		vm.deviceStats.push(deviceStat);
//            });
//        	// Call API
//        	DeviceStat.queryByIds(vm.deviceStats, onSuccess, onError);
//        	function onSuccess(data) {
//        		angular.forEach(vm.devices, function (device, key) {
//        			angular.forEach(data, function (stat, keyStat) {
//        				// wQ
//        				if (device.id == stat.id) {
//        					device.status = stat.wQ;
//        				}
//                    });
//                });
//        		
//        		// No-status
//        		angular.forEach(vm.devices, function (device, key) {
//        			angular.forEach(data, function (stat, keyStat) {
//        				// wQ
//        				if (device.status == "" || device.status == undefined) {
//        					device.status = 'NO-STATUS';
//        				}
//                    });
//                });
//            }
//            function onError(error) {
//                AlertService.error(error.data.message);
//            }
//        }
        
        function searchFilter () {
        	resetCheckbox();
        	if ($rootScope.deviceFilter != null) {
        		var device = $rootScope.deviceFilter;
        		// Update filter
        		device.search = vm.currentSearch;
        		$rootScope.deviceFilter = device;
        	}
        	
        	searchAll ();
        }
        
        function searchAll () {
        	resetCheckbox();
        	var device = {};
        	
        	// Keep filter from root scope
        	if ($rootScope.deviceFilter != null) {
        		device = $rootScope.deviceFilter;
        	} else {
        		device.search = vm.currentSearch;
        		device.page = pagingParams.page - 1,
        		device.size = vm.itemsPerPage;
        		device.sort = sort();
            	
            	// keep filter
            	storeFilterCondition(device);
        	}
        	
        	console.log('searchAll, page:' + device.page);

        	Device.searchWithGridView(device, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.devices = data;
                vm.page = device.page + 1;
                // Load all status
                //loadAllStatus();
                // Load all user
                loadAllUserInDevice();
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        
        function searchAllTransition () {
        	resetCheckbox();
        	var device = {};
        	device.search = vm.currentSearch;
        	device.page = vm.page -1;
        	device.size = vm.itemsPerPage;
        	device.sort = sort();
        	console.log('searchAllTransition, page: ' + device.page);
        	
        	// Keep filter
        	storeFilterCondition(device);
        	
        	Device.searchWithGridView(device, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.devices = data;
                // Load all status
                //loadAllStatus();
                // Load all user
                loadAllUserInDevice();
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        
        function transition() {
        	searchAllTransition();
        }
        
        function storeFilterCondition(device) {
        	$rootScope.deviceFilter = device;
        }
    }
})();

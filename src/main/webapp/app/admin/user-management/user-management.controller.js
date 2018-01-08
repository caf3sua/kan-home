(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('UserManagementController', UserManagementController);

    UserManagementController.$inject = ['$rootScope', 'Principal', 'User', 'ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants', 'JhiLanguageService', '$translate', 'Device', '$localStorage'];

    function UserManagementController($rootScope, Principal, User, ParseLinks, AlertService, $state, pagingParams, paginationConstants, JhiLanguageService, $translate, Device, $localStorage) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_TECHNICAL', 'ROLE_SUPPORTOR'];
        vm.currentAccount = null;
        vm.languages = null;
//        vm.loadAll = loadAll;
        vm.setActive = setActive;
        vm.users = [];
        vm.page = 1;
        vm.totalItems = null;
        vm.clear = clear;
        vm.links = null;
//      vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.transition = transition;
        
        vm.currentSearch = {};
        vm.isSearchShow = true;
        vm.toogleSearch = toogleSearch;
        vm.searchAll = searchAll;
        vm.searchFilter = searchFilter;
        vm.cleanSearch = cleanSearch;
        vm.changeSearchRole = changeSearchRole;
        vm.showAllDeviceId = showAllDeviceId;
        
        
        vm.selectedItem = [];
        vm.checkAll = checkAll;
        vm.checkItem = checkItem;
        
        // Init controller
		(function initController() {
	        JhiLanguageService.getAll().then(function (languages) {
	            vm.languages = languages;
	        });
	        Principal.identity().then(function(account) {
	            vm.currentAccount = account;
	        });
	        
			vm.searchAll();
			loadAllSimpleDevice();
	    })();
		
		function loadAllSimpleDevice() {
	    	
	    		Device.queryWithSimpleData({}, onSaveSuccess, onSaveError);
	        
	        function onSaveSuccess (result) {
	        		$localStorage.all_simple_devices = result;
	        }
	
	        function onSaveError () {
	        }
	    }
        
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
		
        function showAllDeviceId(userDevices) {
        	var result = "";
        	angular.forEach(userDevices, function (device, key) {
        		result = result + device.name + '<br>';
            });
        	
        	return result;
        }
        
        function changeSearchRole() {
        	vm.currentSearch.authorities = [];
        	angular.forEach(vm.authorities, function (auth, key) {
            	if ($('#field_' + auth).is(":checked")) {
            		vm.currentSearch.authorities.push(auth);
            	}
            });
        }

        function cleanSearch() {
        	resetCheckbox();
        	storeFilterCondition(null);
        	vm.currentSearch = {};
        	searchAll();
        }
        
        function toogleSearch() {
        	vm.isSearchShow = !vm.isSearchShow;
        }
        
        function setActive (user, isActivated) {
            user.activated = isActivated;
            User.update(user, function () {
                //vm.loadAll();
            	searchAll();
                vm.clear();
            });
        }

        function clear () {
            vm.user = {
                id: null, login: null, firstName: null, lastName: null, email: null,
                activated: null, langKey: null, createdBy: null, createdDate: null,
                lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                resetKey: null, authorities: null
            };
        }

        function searchFilter () {
        	resetCheckbox();
        	if ($rootScope.userFilter != null) {
        		var user = $rootScope.userFilter;
        		// Update filter
        		user.search = vm.currentSearch;
        		$rootScope.userFilter = user;
        	}
        	
        	searchAll ();
        }
        
        function searchAll () {
        	resetCheckbox();
        	var user = {};
        	
        	// Keep filter from root scope
        	if ($rootScope.userFilter != null) {
        		user = $rootScope.userFilter;
        	} else {
            	user.search = vm.currentSearch;
            	user.page = pagingParams.page - 1,
            	user.size = vm.itemsPerPage;
            	user.sort = sort();
            	
            	// keep filter
            	storeFilterCondition(user);
        	}
        	
        	console.log('searchAll, page:' + user.page);

        	User.search(user, onSuccess, onError);
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
                vm.page = user.page + 1;
                vm.users = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        
        function searchAllTransition () {
        	resetCheckbox();
        	var user = {};
        	user.search = vm.currentSearch;
        	user.page = vm.page -1;
        	user.size = vm.itemsPerPage;
        	user.sort = sort();
        	console.log('searchAllTransition, page: ' + user.page);
        	
        	// Keep filter
        	storeFilterCondition(user);
        	
        	User.search(user, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.users = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        
        function transition() {
        	searchAllTransition();
        }
        
        function storeFilterCondition(user) {
        	$rootScope.userFilter = user;
        }
    }
})();

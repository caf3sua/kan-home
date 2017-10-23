(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Summary', '$translate'];

    function HomeController ($scope, Principal, LoginService, $state, Summary, $translate) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });
        
        // Init controller
		(function initController() {
	        initChart();
	        getAccount();
	    })();
                
        vm.userLabels = [
	        	$translate.instant('home.user-label'),
	        	$translate.instant('home.admin-label'),
	        	$translate.instant('home.technical-label'),
	        	$translate.instant('home.supportor-label')
        	];
        vm.userData;
        vm.userOptions = {
        				legend: {
        					display: true,
        					position: 'bottom'
        				},
        				title: {
        					display: true,
        					text: $translate.instant('home.user-summary')
    				    }
        			};
        
        vm.deviceLabels = [
        	$translate.instant('home.blue-status'),
        	$translate.instant('home.red-status'),
        	$translate.instant('home.yellow-status'),
        	$translate.instant('home.gray-status')
        ];
        vm.deviceData;
        vm.deviceDatasetOverride = [{
        	label:"Minutes"
        }];
        vm.colors = ['#00bfff', '#ff0000', '#ffff00', '#ebebe0'];
        vm.deviceOptions = {
//				legend: {
//					display: true,
//					position: 'bottom'
//				},
				title: {
					display: true,
					text: $translate.instant('home.device-summary')
			    }
			};
        
        
        function initChart() {
        	Summary.query({}, onSaveSuccess, onSaveError);
            
    		function onSaveSuccess (result) {
    			console.log(result);
    	        vm.userData = [result.numberUser, result.numberAdminUser, result.numberTechnicalUser, result.numberSupportorUser];
    	        vm.deviceData = [result.numberBlueDevice, result.numberRedDevice, result.numberYellowDevice, result.numberGrayDevice];
            };

            function onSaveError () {
            }
        }

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                var isAuth = Principal.isAuthenticated();
                if (vm.isAuthenticated == null || vm.isAuthenticated() == false) {
                	$state.go('signin');    	
                }
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();

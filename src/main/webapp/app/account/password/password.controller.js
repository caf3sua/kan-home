(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('PasswordController', PasswordController);

    PasswordController.$inject = ['$rootScope', '$window', '$state', 'Auth', 'Principal', '$mdToast', '$translate'];

    function PasswordController ($rootScope, $window, $state, Auth, Principal, $mdToast, $translate) {
        var vm = this;

        vm.changePassword = changePassword;
        vm.doNotMatch = null;
        vm.error = null;
        vm.success = null;

        Principal.identity().then(function(account) {
            vm.account = account;
        });

        function changePassword () {
        	vm.error = null;
            vm.success = null;
            vm.doNotMatch = null;
            if (vm.password !== vm.confirmPassword) {
                vm.error = null;
                vm.success = null;
                vm.doNotMatch = 'ERROR';
            } else {
                vm.doNotMatch = null;
                var passObj = {};
                passObj.password = vm.password;
                passObj.oldPassword = vm.oldpassword;
                Auth.changePassword(passObj).then(function () {
                    vm.error = null;
                    vm.success = 'OK';
                    var message = $translate.instant('password.messages.success');
                	showMessage(message);
                	
                	// Logout
                	logout();
                }).catch(function () {
                    vm.success = null;
                    vm.error = 'ERROR';
                });
            }
        }
        
        function logout() {
            Auth.logout();
            vm.isAuthenticated = null;
            $rootScope.$broadcast('logoutSuccess');
            $state.go('signin');
            $window.location.reload();
        }
        
        function showMessage(msg) {
        	$mdToast.show(
        		      $mdToast.simple()
        		        .textContent(msg)
        		        //.position(pinTo )
        		        .hideDelay(5000)
        	);
        }
    }
})();

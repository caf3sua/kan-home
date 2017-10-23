(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('UserManagementDeleteController', UserManagementDeleteController);

    UserManagementDeleteController.$inject = ['$uibModalInstance', 'usernames', 'User'];

    function UserManagementDeleteController ($uibModalInstance, usernames, User) {
        var vm = this;

        vm.usernames = usernames;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        (function initController() {
	        console.log(usernames);
	    })();
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete () {
//            User.delete({login: login},
//                function () {
//                    $uibModalInstance.close(true);
//                });
        	
        	console.log(usernames);
            User.deleteUsers(usernames,
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

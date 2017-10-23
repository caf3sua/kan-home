(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('UserManagementDetailController', UserManagementDetailController);

    UserManagementDetailController.$inject = ['$stateParams', 'User', '$window'];

    function UserManagementDetailController($stateParams, User, $window) {
        var vm = this;

        vm.goBack = goBack;
        vm.load = load;
        vm.user = {};

        vm.load($stateParams.login);

        function goBack() {
        	$window.history.back(); 
        }
        
        function load(login) {
            User.get({login: login}, function(result) {
                vm.user = result;
            });
        }
    }
})();

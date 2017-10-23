(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$rootScope', '$scope', '$timeout', '$state', '$window', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function NavbarController ($rootScope, $scope, $timeout, $state, $window, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        $scope.$on('authenticationSuccess', function() {
        	Principal.identity().then(function(account) {
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        });
        
        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        
        function login() {
            collapseNavbar();
            //LoginService.open();
            $state.go('signin');
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            vm.isAuthenticated = null;
            $rootScope.$broadcast('logoutSuccess');
            //$window.location.reload();
            $state.go('signin');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();

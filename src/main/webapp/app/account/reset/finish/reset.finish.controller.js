(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('ResetFinishController', ResetFinishController);

    ResetFinishController.$inject = ['$stateParams', '$timeout', 'Auth', 'LoginService'];

    function ResetFinishController ($stateParams, $timeout, Auth, LoginService) {
        var vm = this;

        vm.keyMissing = angular.isUndefined($stateParams.key);
        vm.confirmPassword = null;
        vm.doNotMatch = null;
        vm.error = null;
        vm.finishReset = finishReset;
        vm.login = LoginService.open;
        vm.resetAccount = {};
        vm.success = null;

        $timeout(function (){angular.element('#password').focus();});

        function finishReset() {
            vm.doNotMatch = null;
            vm.error = null;
            if (vm.resetAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
            	var passMd5 = $.md5(vm.resetAccount.password);
            	var newPassword = $.base64.btoa(passMd5);
                Auth.resetPasswordFinish({key: $stateParams.key, newPassword: newPassword}).then(function () {
                    vm.success = 'OK';
                }).catch(function () {
                    vm.success = null;
                    vm.error = 'ERROR';
                });
            }
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('ActivationController', ActivationController)
    	.controller('ActivationSMSController', ActivationSMSController);

    ActivationController.$inject = ['$stateParams', 'Auth', 'LoginService'];
    ActivationSMSController.$inject = ['$stateParams', 'Auth', 'LoginService', 'Activate'];

    function ActivationController ($stateParams, Auth, LoginService) {
        var vm = this;

        Auth.activateAccount({key: $stateParams.key}).then(function () {
            vm.error = null;
            vm.success = 'OK';
        }).catch(function () {
            vm.success = null;
            vm.error = 'ERROR';
        });

        vm.login = LoginService.open;
    }
    
    function ActivationSMSController ($stateParams, Auth, LoginService, Activate) {
        var vm = this;
        //debugger
        Activate.getSMS({sms: $stateParams.sms, username: $stateParams.username}, onSuccess, onError);
        function onSuccess(data) {
        	vm.error = null;
            vm.success = 'OK';
        }
        function onError(error) {
        	vm.success = null;
            vm.error = 'ERROR';
            AlertService.error(error.data.message);
        }

        vm.login = LoginService.open;
    }
})();

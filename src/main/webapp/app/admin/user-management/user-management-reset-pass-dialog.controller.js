(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('UserManagementResetPassController', UserManagementResetPassController);

    UserManagementResetPassController.$inject = ['$uibModalInstance', 'entity', 'User'];

    function UserManagementResetPassController ($uibModalInstance, entity, User) {
        var vm = this;

        vm.user = entity;
        vm.clear = clear;
        vm.resetPass = resetPass;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function resetPass (login) {
        	// Encrypt pass
        	var newPassMd5 = $.md5(vm.user.password);
        	var encryptNewPass = $.base64.btoa(newPassMd5);

        	vm.user.password = encryptNewPass;
        	//User.save(vm.user, onSaveSuccess, onSaveError);
            User.resetPass(vm.user,
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

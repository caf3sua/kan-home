(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('UserManagementDialogController',UserManagementDialogController);

    UserManagementDialogController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'User', 'JhiLanguageService', '$translate'];

    function UserManagementDialogController ($stateParams, $uibModalInstance, entity, User, JhiLanguageService, $translate) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_TECHNICAL', 'ROLE_SUPPORTOR'];
        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.user = entity;

        vm.genders = [
    		{
    			id: 0,
    			name: $translate.instant('global.form.gender-male')
    		},
    		{
    			id: 1,
    			name: $translate.instant('global.form.gender-female')
    		}
    	];

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save () {
            vm.isSaving = true;
            if (vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }
    }
})();

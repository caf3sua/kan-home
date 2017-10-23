(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['Principal', 'Auth', 'JhiLanguageService', '$translate'];

    function SettingsController (Principal, Auth, JhiLanguageService, $translate) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;
        vm.genders = [
        		{
        			id: 0,
        			name: $translate.instant('settings.form.gender.male')
        		},
        		{
        			id: 1,
        			name: $translate.instant('settings.form.gender.female')
        		}
        	];
        
        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                //firstName: account.firstName,
                langKey: account.langKey,
                //lastName: account.lastName,
                username: account.username,
                fullname : account.fullname,
                phonenumber : account.phonenumber,
                gender: account.gender,
                address: account.address
            };
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });

        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                });
                JhiLanguageService.getCurrent().then(function(current) {
                    if (vm.settingsAccount.langKey !== current) {
                        $translate.use(vm.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }
    }
})();

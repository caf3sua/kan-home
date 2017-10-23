(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('JhiLanguageController', JhiLanguageController);

    JhiLanguageController.$inject = ['$translate', 'JhiLanguageService', 'tmhDynamicLocale'];

    function JhiLanguageController ($translate, JhiLanguageService, tmhDynamicLocale) {
        var vm = this;

        vm.changeLanguage = changeLanguage;
        vm.languages = null;
        vm.currentLang = currentLang;

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function changeLanguage (languageKey) {
            $translate.use(languageKey);
            tmhDynamicLocale.set(languageKey);
        }
        
        function currentLang () {
        	var currentLang = $translate.proposedLanguage() || $translate.use();
        	return currentLang;
        }
    }
})();

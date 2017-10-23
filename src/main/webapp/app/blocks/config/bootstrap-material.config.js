(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .config(bootstrapMaterialDesignConfig);

//    compileServiceConfig.$inject = [];
//
//    function bootstrapMaterialDesignConfig() {
//        $.material.init();
//
//    }

    bootstrapMaterialDesignConfig.$inject = [];

    function bootstrapMaterialDesignConfig() {
        $.material.init();
    }
})();

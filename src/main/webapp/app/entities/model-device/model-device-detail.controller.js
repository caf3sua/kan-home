(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('ModelDeviceDetailController', ModelDeviceDetailController);

    ModelDeviceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ModelDevice'];

    function ModelDeviceDetailController($scope, $rootScope, $stateParams, previousState, entity, ModelDevice) {
        var vm = this;

        vm.modelDevice = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kanHomeApp:modelDeviceUpdate', function(event, result) {
            vm.modelDevice = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

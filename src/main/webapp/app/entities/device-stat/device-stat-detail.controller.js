(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('DeviceStatDetailController', DeviceStatDetailController);

    DeviceStatDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DeviceStat'];

    function DeviceStatDetailController($scope, $rootScope, $stateParams, previousState, entity, DeviceStat) {
        var vm = this;

        vm.deviceStat = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('kanHomeApp:deviceStatUpdate', function(event, result) {
            vm.deviceStat = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

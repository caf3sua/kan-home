(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .controller('DeviceDetailController', DeviceDetailController);

    DeviceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Device', 'Country', 'City', 'ModelDevice'];

    function DeviceDetailController($scope, $rootScope, $stateParams, previousState, entity, Device, Country, City, ModelDevice) {
        var vm = this;

        vm.device = entity;
        vm.previousState = previousState.name;

        init();
        
        function init() {
        	Country.getbyCountryCode({countryCode: vm.device.countryCode}, function(result) {
        		vm.device.countryCodeObj = result;
        	});
        	
        	ModelDevice.getByModel({model: vm.device.model}, function(result) {
        		vm.device.modelObj = result;
        	});
        	
        	City.get({id: vm.device.cityCode}, function(result) {
        		vm.device.cityObj = result;
        	});
        };
        
        var unsubscribe = $rootScope.$on('kanHomeApp:deviceUpdate', function(event, result) {
            vm.device = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
